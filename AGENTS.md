# Project Overview
- Whatting 프로젝트는 학교의 재난상황에 학생들의 인원 파악을 쉽게 도와주는 서비스이다.
- 주요 스택: Java + Spring Boot 등

# General Working Rules
- 작업 전 `git branch`와 `git status`를 확인한다.
- 요청된 작업 범위 내에서만 작업한다.
- 불필요한 기능 추가/리팩터링은 지양한다.
- 프론트엔드 작업과 백엔드 작업 브랜치를 나누는 것을 지향한다.

# Done Definition
- 변경 파일 요약
- 구현 내용 요약
- 테스트 방법
- 남은 리스크/TODO

# git Rules
- default branch는 develop이다.
- 기능 작업은 항상 최신 develop branch 기준으로 새 브랜치를 파생하여 작업한다.

## branch Naming Rules
- short-description: 작업 내용을 짧고 명확하게 작성한다.
- 소문자 영어와 -를 사용한다.
- 너무 길게 작성하지 않는다.
- 브랜치명은 작업 목적이 드러나게 작성합니다.

- area: 작업 영역
    - backend
    - docs
    - infra
    - common
- type: 작업 종류
    - feature
    - fix
    - refactor
    - chore
    - docs
    - style
    - test

| Type       | Meaning                | Example                                    |
| ---------- |------------------------|--------------------------------------------|
| `feature`  | 새로운 기능 추가              | `backend/feature/item-submission-api`      |
| `fix`      | 버그 수정                  | `backend/fix/item-submission-status-error` |
| `refactor` | 기능 변화 없는 구조 개선         | `backend/refactor/storage-service`         |
| `chore`    | 개발환경, 설정, 빌드, 의존성 등 작업 | `backend/chore/swagger-config`             |
| `docs`     | 문서 작성/수정               | `docs/update-agents-guide`                 |

## commit Rules
- 너무 작은 수정마다 커밋하지 않고, 의미 있는 작업 단위로 커밋한다.
- 기능 1차 구현, 버그 수정, 설정 변경, 문서 수정은 가능하면 분리한다.
- 여러 작업이 섞이면 커밋을 나누는 것을 권장한다.
- 빌드가 온전하지 않은 상태의 커밋은 지양한다.

### Commit Message Types
기본 형식은 `type :: short description`입니다.

- 설명은 필요한 기술 용어만 영어로 쓰고, 내용은 한글 작성을 지향.
- 제목은 너무 길어지지 않도록 지향.

| Type | When To Use | Example                            |
| --- | --- |------------------------------------|
| `feat` | 새로운 기능 추가 | `feat :: 회원가입 API 추가`              |
| `fix` | 버그 수정 | `fix :: 경보 생성 Swagger 인증 설정 수정`    |
| `refactor` | 기능 변화 없이 코드 구조 개선 | `refactor :: User service 분리`      |
| `chore` | 빌드, 설정, 의존성, 개발환경 변경 | `chore :: redis 의존성 추가`            |
| `docs` | 문서 수정 | `docs :: Git 컨벤션 추가`               |
| `style` | 포맷팅, 세미콜론, 들여쓰기 등 로직 변화 없는 수정 | `style :: user controller 포맷 정리`   |
| `rename` | 파일/패키지명 변경 | `rename :: user request dto 이름 변경` |
| `remove` | 불필요한 코드/파일 삭제 | `remove :: 사용하지 않는 클래스 삭제`         |

## Push
- `push`는 기능단위로 할 수 있도록 권장한다.
    - 필요한 경우엔 어느정도 commit이 안정화 되어있다면 push한다.
- `develop` 병합 전에는 작업 브랜치를 원격에 push한다.
- 빌드 실패 상태를 원격에 올리는 것은 가능하면 지양한다.

### Codex Commit/push Rules
- Codex는 사용자가 명시적으로 커밋을 요청한 경우에만 `commit`을 수행한다.
- 커밋 전 반드시 `git status`로 변경사항을 확인한다.
- 커밋 전 변경 파일 목록과 포함 범위를 사용자에게 요약한다.
- 커밋 메시지는 이 문서의 컨벤션에 맞춰 제안하거나 작성한다.
- `git push`는 사용자가 명시적으로 요청한 경우에만 수행한다.
- 사용자 승인 없이 `add`, `commit`, `push`, `reset`, `rebase`, `restore`, `force push`를 수행하지 않는다.

# Backend package structure
domain(p)
- 기능
  - domain(p)
    - FeatureName(Entity)
  - exception(p)
    - FeatureException
  - presentation(p)
    - dto(p)
      - request(p)
        - FeatureRequest
      - response(p)
        - FeatureResponse
    - FeatureController
  - repository(p)
    - FeatureRepository
  - service(p)
    - FeatureService
global