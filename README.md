Monthly Coffee Project
======
#### 사용된 기술 및 언어
![java](https://img.shields.io/badge/java-FF9900.svg?style=for-the-badge&logo=JAVA&logoColor=white&logoWidth=20)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white&logoWidth=20)
![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F.svg?style=for-the-badge&logo=SpringBoot&logoColor=white&logoWidth=20)
![JPA](https://img.shields.io/badge/JPA-02303A.svg?style=for-the-badge&logo=JPA&logoColor=white&logoWidth=20)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

![MySQL](https://img.shields.io/badge/MySQL-4479A1.svg?style=for-the-badge&logo=MySQL&logoColor=white&logoWidth=20)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)

![swagger](https://img.shields.io/badge/swagger-85EA2D.svg?style=for-the-badge&logo=swagger&logoColor=white&logoWidth=20)
#### 코드 및 버전관리
![github](https://img.shields.io/badge/github-181717.svg?style=for-the-badge&logo=github&logoColor=white&logoWidth=20)
![git](https://img.shields.io/badge/git-F05032.svg?style=for-the-badge&logo=git&logoColor=white&logoWidth=20)
#### 협업 툴
![slack](https://img.shields.io/badge/slack-4A154B.svg?style=for-the-badge&logo=slack&logoColor=white&logoWidth=20) <br/>

#### ER 다이어그램
```mermaid
erDiagram
    MemberInfo ||--o{ BudgetInfo : places
    MemberInfo ||--o{ IncomeInfo : places
    MemberInfo ||--o{ ExpenseInfo : places 
    MemberInfo ||--o{ CommentInfo : writes
    MemberInfo ||--o{ LovePostInfo : places
    ExpenseInfo ||--o| PostInfo : writes
    ExpenseInfo ||--o{ ExpenseImageInfo : has
    PostInfo ||--o{ CommentInfo : has
    PostInfo ||--o{ LovePostInfo : has
    MemberInfo {
        Long id
        AuthDomain authDomain
        String uid
        String nickname
        LocalDate birth
        Gender gender
    }
    IncomeInfo {
        Long id
        Integer amount
        String note
        LocalDate date
    }
    BudgetInfo {
        Long id
        Integer amount
        String yearMonth
    }
    ExpenseInfo {
        Long id
        String category
        String brand
        Integer price
        String memo
        Boolean tumbler
        Taste taste
        Mood mood
        CoffeeBean bean
        LikeHate likeHate
        Integer payment
        LocalDate date
    }
    LovePostInfo {
         Long id
    }
    PostInfo {
        Long id
        String content
    }
    CommentInfo {
        Long id
        String content
    }
    ExpenseImageInfo {
        Long id
        String filename
        String originalName
    }
```