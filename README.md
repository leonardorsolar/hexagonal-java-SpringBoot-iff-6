Claro! Aqui está sua **terceira etapa** revisada com mais clareza, fluidez e padronização:

---

# Tutorial Arquitetura Hexagonal - CRUD de Usuários | API + MongoDB (NoSQL) + Kafka (Mensageria)

Aprenda na prática como aplicar a **Arquitetura Hexagonal** em microsserviços utilizando **Java**, **Spring Boot**, **MongoDB** e **Kafka**.

Neste projeto, construiremos um **CRUD de Clientes**, explorando todas as camadas da arquitetura de forma clara e orientada.

---

## 🔁 Etapa 6: Camada de _Infrastructure_ — Criação do Adaptador _Controller_

Após modelarmos as classes de domínio `Customer` e `Address`, o caso de uso `CreateCustomerUseCase` , adaptador de saída `AddressLookupOutputPort` para buscar o CEP, o **adaptador da porta de saída** responsável por acessar o banco de dados, nosso próximo passo é criar o **adaptador de entrada** responsável por inserir o cliente.

---

### 🎯 Objetivos da Etapa

---

## ✏️ Parte 1: Criando a controller

Crie as entidades dentro do pacote de entrada controller:

```
src/main/java/com/example/hexagonal/infrastructure/adapter/input/controller
```

### `CustomerController.java`

```java
package com.example.hexagonal.infrastructure.adapter.input.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @PostMapping
    public ResponseEntity<Void> createCustomer() {
    }
}
```

@RestController
@RequestMapping("/api/v1/customers") para definir qual seria o path do nosso endpoint
@PostMapping método para inserção

Como parametro precisamos criar uma classe de request para pegarmos os dados que precisamos inserir na base de dados

### Criando a classe de request:

criar a pasta request e dentro dela crie a classe CustomerRequestDTO.java
src/main/java/com/example/hexagonal/infrastructure/adapter/input/controller/request/CustomerRequestDTO.java

```java
package com.example.hexagonal.infrastructure.adapter.input.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String cpf;

    @NotBlank
    private String zipCode;
}
```

o id o mongo gera automatico
o endereço buscaremos de acordo com o cep enviado.

Estes serão os dados que vamos receber por parâmetros na minha requisição.

@Data
@NotBlank para validações

Sim! ✅ A classe `CustomerRequestDTO` é um **DTO** — mais precisamente, um **Request DTO** (Data Transfer Object).

---

### 📌 O que é um DTO?

Um **DTO (Data Transfer Object)** é uma classe usada para **transportar dados entre camadas**, especialmente **entre o mundo externo e a aplicação**.

No seu caso, o `CustomerRequestDTO` é um DTO que representa os **dados recebidos na requisição HTTP**, ou seja, **o corpo da requisição (JSON)** enviado pelo cliente.

---

### ✅ Por que usar um Request DTO?

-   **Evita acoplamento** entre a API e o domínio.
-   Permite **validação** dos dados com anotações como `@NotBlank`.
-   Facilita a manutenção e a separação de responsabilidades.
-   Garante que a entrada da API seja controlada, limpa e validada antes de ser usada no `use case`.

---

### 📁 Onde ele é usado?

O `CustomerRequestDTO` será utilizado no seu **controller**, assim:

```java
@PostMapping
public ResponseEntity<Void> createCustomer(@RequestBody @Valid CustomerRequestDTO request) {
    useCase.create(request.toDomain(), request.getZipCode());
    return ResponseEntity.status(HttpStatus.CREATED).build();
}
```

CustomerRequestDTO request será um @RequestBody e para pegar as validações @Valid
depois precisamos acessar nosso caso de uso para podermos inserir o cliente
Porém não podemso acessar nosso caso de uso diretamente para não ter acoplamento, assim teremos que criar uma porta de entrada para acessarmos

### Criando a porta de entrada para a controller na camada application:

Em src/main/java/com/example/hexagonal/application/port/input

Crie uma nova interface CreateCustomerInputPort.java

```java
package com.example.hexagonal.application.port.input;

import com.example.hexagonal.domain.Customer;

public interface CreateCustomerInputPort {

    void create(Customer customer, String zipCode);

}
```

Assim nosso caso de uso terá que implementar esta porta para podermos acessar o método.

### Ajustando o caso de uso para implementar a porta de entrada da controller

Acesse : src/main/java/com/example/hexagonal/application/usecase/CreateCustomerUseCase.java

Adicone: implements CreateCustomerInputPort no arquivo CreateCustomerUseCase.java

```java
public class CreateCustomerUseCase implements CreateCustomerInputPort
```

```java
package com.example.hexagonal.application.usecase;

import com.example.hexagonal.application.port.input.CreateCustomerInputPort;
import com.example.hexagonal.application.port.output.AddressLookupOutputPort;
import com.example.hexagonal.application.port.output.CustomerPersistenceOutputPort;
import com.example.hexagonal.domain.Customer;

public class CreateCustomerUseCase implements CreateCustomerInputPort {

    private final AddressLookupOutputPort addressLookupOutputPort;
    private final CustomerPersistenceOutputPort customerPersistenceOutputPort;

    public CreateCustomerUseCase(AddressLookupOutputPort addressLookupOutputPort,
            CustomerPersistenceOutputPort customerPersistenceOutputPort) {
        this.addressLookupOutputPort = addressLookupOutputPort;
        this.customerPersistenceOutputPort = customerPersistenceOutputPort;
    }

    // this.cpfValidationMessagePort = cpfValidationMessagePort;

    public void create(Customer customer, String zipCode) {
        var address = addressLookupOutputPort.findByZipCode(zipCode);
        customer.setAddress(address);
        customerPersistenceOutputPort.save(customer);
        // cpfValidationMessagePort.sendCpfForValidation(customer.getCpf());
    }

}
```

Agora já podemos injetar nossa porta de entrada (`CreateCustomerInputPort`) na controller para acessarmos o caso de uso

### Implementando o médodo da controller:

Acesse: src/main/java/com/example/hexagonal/infrastructure/adapter/input/controller/CustomerController.java

```java
@Autowired
    private CreateCustomerInputPort createCustomerInputPort;

    @PostMapping
    public ResponseEntity<Void> createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        createCustomerInputPort.create();
    }
```

Veja que precisaremos de customer como parâmetro em createCustomerInputPort.create() e não um customerRequestDTO
então precisremos criar um mapper para transformar CustomerRequestDTO em customer

### Criando o mapper:

Crie a pasta mapper em src/main/java/com/example/hexagonal/infrastructure/adapter/input/controller/mapper
Dentro de mapper crie uma interface chamado de CustomerMapper
src/main/java/com/example/hexagonal/infrastructure/adapter/input/controller/mapper/CustomerMapper.java

Observe que o

-   CustomerRequestDTO tem name, cpf e zipCode e o
-   Customer tem id name, address, cpf e isValidCpf

precisamos ignorar os campos is, address e isValidCpf se não dará erro ( com o @Mapping)

```java
package com.example.hexagonal.infrastructure.adapter.input.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.hexagonal.domain.Customer;
import com.example.hexagonal.infrastructure.adapter.input.controller.request.CustomerRequestDTO;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "isValidCpf", ignore = true)

    Customer toCustomer(CustomerRequestDTO customerRequest);

}
```

Podemos agora injetar o CustomerMapper na controller e incluir no método

```java
package com.example.hexagonal.infrastructure.adapter.input.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hexagonal.application.port.input.CreateCustomerInputPort;
import com.example.hexagonal.infrastructure.adapter.input.controller.mapper.CustomerMapper;
import com.example.hexagonal.infrastructure.adapter.input.controller.request.CustomerRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CreateCustomerInputPort createCustomerInputPort;

    @Autowired
    private CustomerMapper customerMapper;

    @PostMapping
    public ResponseEntity<Void> createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        var customer = customerMapper.toCustomer(customerRequestDTO);
        createCustomerInputPort.create(customer, customerRequestDTO.getZipCode());
        return ResponseEntity.ok().build();
    }
}
```

---

## ✏️ Parte 2: Criar um teste de integração real da controller

Em construção

### 📌 Próximos passos:

7. **Criar o CRUD (Read, Update. Delete)**

    - Desafio para vocês incrementarem o Read, Update. Delete
    - Buscar o cliente por id
    - Buscar a lista de cliente
    - Atualizar os dados do cliente
    - Deletar o cliente

    Dica: crie primeiro o usecase, depois o adapter e depois o controller

8. **Criação das configurações**

    - Criando os beans do CRUD
    - Criando as configurações do Kafla

9. **Criação do producer e consumer do kafka**

10. **Configuração do ambeinte para rodar a aplicação**

11. **Proteção da Arquitetura**

---

https://github.com/DaniloArantesSilva/hexagonal-architecture
