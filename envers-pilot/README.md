# [Hibernate Envers](https://hibernate.org/orm/envers/)

- 엔터티 클래스에 대한 감사/버전 관리 솔루션을 제공하는 모듈

## 특징

- JPA 에서 정의된 모든 매핑의 감사(Auditing)
- 사용자 타입, 문자열, 컬렉션(Collection) 등과 같은 JPA를 확장한 하이버네이트 매핑들을 감사
- 리비전(Revision) 엔티티를 사용하여 데이터를 기록
- 엔티티 및 연관되 데이터의 과거 스냅샷을 조회

# [Spring Data Envers](https://docs.spring.io/spring-data/envers/docs/current/reference/html/#envers.what)

- Spring Framework 에서 제공하는 확장 기능
- Spring Data JPA와 같이 사용할 수 있다.

build.gradle 의존성 추가
```groovy
implementation 'org.springframework.data:spring-data-envers'
```

### REVTYPE column values

| Database column value | RevisionType Enum value |
|-----------------------|-------------------------|
| 0                     | ADD                     |
| 1                     | MOD                     |
| 2                     | DEL                     |

[RevisionRepository Docs](https://docs.spring.io/spring-data/commons/docs/current/api//org/springframework/data/repository/history/RevisionRepository.html)

## RevisionEntity

- `RevisionEntity` 여러개라면 예외발생
- `RevisionListener`를 통해 엔터티의 인스턴스를 생성하는 방법 정의
- `@RevisionNumber` 필요

## 단점

1. 기존 테이블에 alter 가 발생하면 revision 테이블에도 변경사항을 적용해야 한다.
2. Bulk 연산에는 적용되지 않는다.
3. 연관관계 엔티티에서 @Audited 를 선언하지 안은경우 예외 발생
