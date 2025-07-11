Claro! Aqui está sua **terceira etapa** revisada com mais clareza, fluidez e padronização:

---

# Tutorial Arquitetura Hexagonal - CRUD de Usuários | API + MongoDB (NoSQL) + Kafka (Mensageria)

Aprenda na prática como aplicar a **Arquitetura Hexagonal** em microsserviços utilizando **Java**, **Spring Boot**, **MongoDB** e **Kafka**.

Neste projeto, construiremos um **CRUD de Clientes**, explorando todas as camadas da arquitetura de forma clara e orientada.

---

## 🔁 Etapa 5: Camada de _Infrastructure_ — Criação do Adaptador _Repository_

Após modelarmos as classes de domínio `Customer` e `Address`, o caso de uso `CreateCustomerUseCase` e o adaptador de saída `AddressLookupOutputPort` para buscar o CEP, nosso próximo passo é criar o **adaptador da porta de saída** responsável por acessar o banco de dados. Esse adaptador será a implementação da interface `CustomerPersistenceOutputPort`.

-   Será feita uma **implementação concreta do repositório utilizando MongoDB**. `MongoCustomerRepositoryAdapter`

Esse adaptador terá como responsabilidade realizar operações de persistência e consulta de dados no banco **MongoDB**, conforme definido pelo contrato da interface de saída.

---

---

Ótima pergunta! Aqui estão os **objetivos da Etapa 5: Criação do Adaptador Repository na camada de Infrastructure**, considerando o uso do MongoDB e os princípios da arquitetura hexagonal:

---

### 🎯 Objetivos da Etapa

-   ✅ **Implementar o adaptador da porta de saída `CustomerPersistenceOutputPort`**, criando a classe `MongoCustomerRepositoryAdapter` responsável por interagir com o banco de dados MongoDB.

-   ✅ **Garantir o isolamento do domínio em relação à tecnologia de persistência**, mantendo a independência da lógica de negócio em relação ao banco de dados utilizado.

-   ✅ **Facilitar testes e manutenção**, permitindo que a persistência possa ser facilmente substituída por outra tecnologia (como PostgreSQL, MySQL ou até armazenamento em memória) sem impactar o domínio ou os casos de uso.

-   ✅ **Aplicar os princípios da arquitetura hexagonal**, desacoplando o núcleo da aplicação das implementações externas.

-   ✅ **Encapsular a lógica de acesso ao banco de dados**, concentrando as operações de leitura e escrita em um único componente adaptador, tornando o código mais coeso e reutilizável.

---

Perfeito! Abaixo está a versão **ajustada e didática** do tutorial, com explicações claras para iniciantes, seguindo a arquitetura hexagonal e usando Spring Boot + MongoDB:

---

## ✅ Configuração da Camada de Persistência com MongoDB (Spring Data)

Vamos configurar o MongoDB como banco de dados para persistir os dados da aplicação. Lembre-se: na arquitetura hexagonal, a **infraestrutura (banco de dados)** deve ser acessada apenas por **adaptadores**, e nunca diretamente pelo domínio ou pelos casos de uso.

---

## ✏️ Parte 1: Configuração da URI do MongoDB

Abra o arquivo `application.properties` ou `application.yml` dentro da pasta:

```
src/main/resources/
```

E adicione a URI de conexão com o MongoDB:

### Para `application.properties`:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/hexagonal
```

utilizaremos o de cima do que o arquivo yml

### Para arquivos .yml `application.yml` (alternativa):

```yaml
spring:
    data:
        mongodb:
            uri: mongodb://localhost:27017/hexagonal
```

Essa URI diz ao Spring Boot para se conectar a um banco MongoDB rodando localmente na porta 27017, usando o banco chamado `hexagonal`.

---

## ✏️ Parte 2: Criação das classes **Entity**

> No MongoDB, os dados são armazenados em coleções (semelhante a tabelas em bancos relacionais).
> Essas entidades não fazem parte do **domínio**, pois estão ligadas à forma como os dados são **armazenados**, ou seja, pertencem à **camada de infraestrutura**.

Crie as entidades dentro do pacote:

```
src/main/java/com/example/hexagonal/infrastructure/adapter/output/repository/entity
```

### `CustomerEntity.java`

```java
package com.example.hexagonal.infrastructure.adapter.output.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "customers") // Define o nome da coleção no MongoDB
public class CustomerEntity {

    @Id
    private String id;

    private String name;

    private AddressEntity address;

    private String cpf;

    private Boolean isValidCpf;
}
```

Claro! Aqui está uma explicação objetiva e resumida:

---

### 🔎 O que é `CustomerEntity.java`?

A classe `CustomerEntity` representa **como o cliente será salvo no MongoDB**.

Ela é uma **entidade da camada de infraestrutura**, usada apenas para persistência dos dados.

---

### 🧱 Detalhes do código:

-   `@Document(collection = "customers")`: indica que os dados serão salvos na coleção `customers` no MongoDB.
-   `@Id`: define o campo `id` como identificador único do documento.
-   `AddressEntity`: endereço do cliente, definido como um objeto aninhado.

> 💡 Essa classe não deve ser usada no domínio. Ela é específica para o banco de dados.

Vamos agora criar a classe `AddressEntity.java`

### `AddressEntity.java`

```java
package com.example.hexagonal.infrastructure.adapter.output.repository.entity;

import lombok.Data;

@Data
public class AddressEntity {
    private String street;
    private String city;
    private String state;
}
```

---

## ✏️ Parte 3: Criação da Interface de Repositório de mapeamento dos métodos de acesso ao MongoDB

O **Spring Data MongoDB** precisa da **interface** para gerar automaticamente os métodos de acesso ao banco

### 📁 Caminho:

```
src/main/java/com/example/hexagonal/infrastructure/adapter/output/repository
```

### 🧱 Passo 1: Interface de Repositório

Essa interface será usada pelo Spring Data para mapear automaticamente os métodos de acesso ao MongoDB:

crie o arquivo MongoCustomerRepository.java em src/main/java/com/example/hexagonal/infrastructure/adapter/output/repository

```java
package com.example.hexagonal.infrastructure.adapter.output.repository;

import com.example.hexagonal.infrastructure.adapter.output.repository.entity.CustomerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoCustomerRepository extends MongoRepository<CustomerEntity, String> {
}
```

---

### 🧱 Por que criar a `MongoCustomerRepository.java`?

O **Spring Data MongoDB** precisa dessa **interface** para gerar automaticamente os métodos de acesso ao banco, como:

-   `save()`
-   `findById()`
-   `deleteById()`
-   `findAll()`

---

### ✅ O que ela faz?

Essa interface **conecta o Spring Boot ao MongoDB**, sem que você precise escrever consultas manuais.

> Você só diz **qual entidade** (no caso, `CustomerEntity`) e o **tipo da chave** (`String`), e o Spring cuida do resto.

---

### 📌 Em resumo:

> A `MongoCustomerRepository` é a ponte que o Spring usa para ler e gravar clientes no MongoDB automaticamente.

# Criando o adapatador da porta de saída (CustomerPersistenceOutputPort) de inserção do cliente

## ✏️ Parte 3: Criação do Adaptador de Saída no repositório

> A camada de aplicação já possui uma **porta de saída** chamada `CustomerPersistenceOutputPort`, que define o contrato para salvar o cliente.
> Agora vamos criar sua implementação concreta, que **interage com o MongoDB**, chamada `MongoCustomerRepositoryAdapter`.

### 🧱 Passo 1: Adaptador `MongoCustomerRepositoryAdapter`

Crie o arquivo MongoCustomerRepositoryAdapter.java em:
src/main/java/com/example/hexagonal/infrastructure/adapter/output/MongoCustomerRepositoryAdapter.java

Agora sim, o **adaptador real** da porta de saída:

```java
package com.example.hexagonal.infrastructure.adapter.output;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.hexagonal.application.port.output.CustomerPersistenceOutputPort;
import com.example.hexagonal.domain.Customer;
import com.example.hexagonal.infrastructure.adapter.output.repository.MongoCustomerRepository;
import com.example.hexagonal.infrastructure.adapter.output.repository.entity.CustomerEntity;
import com.example.hexagonal.infrastructure.adapter.output.repository.mapper.CustomerEntityMapper;

// pensar em nomear MongoCreateCustomerRepositoryAdapter
@Component
public class MongoCustomerRepositoryAdapter implements CustomerPersistenceOutputPort {

    @Autowired
    private MongoCustomerRepository repository;

    @Autowired
    private CustomerEntityMapper mapper;

    @Override
    public void save(Customer customer) {
        CustomerEntity entity = mapper.toCustomerEntity(customer);
        repository.save(entity);
    }
}
```

Foi injetados o repositorio para o método utilizar o repository

@Component para a classe ser gerenciada pelo spring
@Autowired

compare a diferença de utilização ou não decorator do spring boot.

```java
@Component
public class MongoCustomerRepositoryAdapter implements CustomerPersistenceOutputPort {

    private final MongoCustomerRepository repository;

    private final CustomerEntityMapper mapper;

    public MongoCustomerRepositoryAdapter(MongoCustomerRepository repository, CustomerEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(Customer customer) {
        CustomerEntity entity = mapper.toCustomerEntity(customer);
        repository.save(entity);
    }
}
```

Oberve abaixo:

```java
@Override
    public void save(Customer customer) {
        MongoCustomerRepository.save();

    }
```

Quando criamos o MongoCustomerRepository, criamos as classe de entidades CustomerEntity e AddressEntity
A classe CustomerEntity que será salva na base de dados.
Para que possamos salvar, tereos que criar um mapper para transforma customer em CustomerEntity

```java
 @Override
    public void save(Customer customer) {
        CustomerEntity entity = mapper.toCustomerEntity(customer);
        repository.save(entity);
    }
```

Assim está pronto no adapatador de inserção de cliente

---

## ✅ Observação sobre o Mapper

Como estamos convertendo entre `Customer` (domínio) e `CustomerEntity` (infra), é recomendado criar um **mapper**.

---

## 🛠️ Crie o arquivo:

`src/main/java/com/example/hexagonal/infrastructure/adapter/output/repository/mapper/CustomerEntityMapper.java`

Usando o Spring boot:

```java
package com.example.hexagonal.infrastructure.adapter.output.repository.mapper;

import com.example.hexagonal.domain.Customer;
import com.example.hexagonal.infrastructure.adapter.output.repository.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {

    // eu recebo um Customer e retorno um CustomerEntity
    CustomerEntity toCustomerEntity(Customer customer);

}
```

Vamos optar com spring boot, mas veja o sem Spring boot:

```java
package com.example.hexagonal.infrastructure.adapter.output.repository.mapper;

import org.springframework.stereotype.Component;

import com.example.hexagonal.domain.Address;
import com.example.hexagonal.domain.Customer;
import com.example.hexagonal.infrastructure.adapter.output.repository.entity.AddressEntity;
import com.example.hexagonal.infrastructure.adapter.output.repository.entity.CustomerEntity;

@Component
public class CustomerEntityMapper {

    // Domínio -> Entidade (para salvar no banco)
    public CustomerEntity toEntity(Customer customer) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setStreet(customer.getAddress().getStreet());
        addressEntity.setCity(customer.getAddress().getCity());
        addressEntity.setState(customer.getAddress().getState());

        CustomerEntity entity = new CustomerEntity();
        entity.setId(customer.getId());
        entity.setName(customer.getName());
        entity.setCpf(customer.getCpf());
        entity.setIsValidCpf(customer.getIsValidCpf());
        entity.setAddress(addressEntity);

        return entity;
    }

    // Entidade -> Domínio (para uso na aplicação)
    public Customer toDomain(CustomerEntity entity) {
        Address address = new Address();
        address.setStreet(entity.getAddress().getStreet());
        address.setCity(entity.getAddress().getCity());
        address.setState(entity.getAddress().getState());

        Customer customer = new Customer();
        customer.setId(entity.getId());
        customer.setName(entity.getName());
        customer.setCpf(entity.getCpf());
        customer.setIsValidCpf(entity.getIsValidCpf());
        customer.setAddress(address);

        return customer;
    }
}
```

---

### ✅ O que faz o `CustomerEntityMapper`?

| Método       | Função                                                                                  |
| ------------ | --------------------------------------------------------------------------------------- |
| `toEntity()` | Converte um `Customer` (domínio) em `CustomerEntity` (infra) para **salvar no banco**   |
| `toDomain()` | Converte um `CustomerEntity` (infra) em `Customer` (domínio) para **usar na aplicação** |

> 💡 Ele mantém o **domínio independente da tecnologia de persistência**, seguindo a proposta da arquitetura hexagonal.

---

## ✏️ Parte 4: Criar um teste de integração real (chamando a API)

Em construção

### 📌 Próximos passos:

7. **Criar o Controller (porta de entrada)**

    - Para expor o endpoint REST e permitir a criação de clientes via HTTP.

---

Se quiser, posso escrever a estrutura da classe `CreateCustomerUseCase` para você com exemplos. Deseja isso?

https://github.com/DaniloArantesSilva/hexagonal-architecture
