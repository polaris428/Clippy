# 📋 클리피 (Clippy)

> **사용자의 복사, 저장, 공유까지 한 번에.**  
> 클리피는 클립보드 내용을 자동 저장하고, 폴더 기반 정리 및 공유 기능까지 제공하는 Android 클립보드 관리 앱입니다.

---

## 🌟 주요 기능 (Key Features)

- ✅ **클립보드 자동 저장**: 복사한 내용이 자동으로 앱에 저장되어 필요할 때 쉽게 꺼내 쓸 수 있음
- ✅ **폴더 기반 정리**: 사용자가 직접 폴더를 만들어 클립보드를 정리 가능
- ✅ **공유 폴더 기능**: Firebase 연동을 통해 다른 사용자와 클립보드를 실시간 공유
- ✅ **클립보드 미리보기 및 검색**: 저장된 클립보드를 내용/날짜 기반으로 빠르게 검색 가능
- ✅ **커스텀 UI 및 애니메이션**: 저장 완료 시 애니메이션 등 직관적이고 미려한 UI 제공

---

## 📸 스크린샷 (Screenshots)

| 메인 화면 | 저장 기능 |저장 기능 GIF|
|-----------|-----------|-----------|
| ![image](https://github.com/user-attachments/assets/203b8bf0-54a1-4835-9368-fe20bd329c64)| ![image](https://github.com/user-attachments/assets/9b880206-baea-4882-a5e9-c604847b6b4e) | ![클리피 영상_2](https://github.com/user-attachments/assets/fe6370d7-c05c-4be1-9f09-fcc3a1017f11)| |






## 🔧 기술 스택 (Tech Stack)

- **언어**: Kotlin
- **UI**: Jetpack Compose
- **아키텍처**: MVI + Clean Architecture
- **비동기 처리**: Coroutine + Flow
- **DI(의존성 주입)**: Hilt
- **네비게이션**: Navigation Compose
- **로컬 저장소**: Room DB
- **실시간 동기화**: Firebase Realtime Database
- **이미지 처리**: Glide
- **애니메이션**: Lottie, Custom Compose Animation
- **디자인 시스템**: Custom Compose Components
- **모듈화**: 기능 단위로 구성된 **멀티 모듈 아키텍처**

---

## 🗂 프로젝트 구조 (Project Structure)

```
📦 Clippy Project
┣ 📱 :app                  # 앱 실행 진입점
┣ 📂 feature
┃ ┣ 📁 main                    # 전체 화면의 루트 및 라우팅 중심
┃ ┣ 📁 main_save              # 메인 화면 전환 처리 (초기화 포함)
┃ ┣ 📁 sign_in                # 로그인 화면
┃ ┣ 📁 splash                 # 스플래시
┃ ┣ 📁 clipboard              # 클립보드 메인 기능
┃ ┣ 📁 clipboard_list         # 클립보드 리스트
┃ ┣ 📁 clipboard_edit         # 클립보드 수정
┃ ┣ 📁 clipboard_save_animation # 저장 애니메이션 화면
┃ ┣ 📁 clipboard_shared_list  # 공유 클립보드 리스트
┃ ┣ 📁 folder_edit            # 폴더 수정
┃ ┣ 📁 folder_join            # 폴더 참여
┃ ┣ 📁 folder_setting         # 폴더 설정
┃ ┗ 📁 main_slide_panel       # 사이드 패널
┣ 📂 core
┃ ┣ 📁 model                  # 데이터 모델 (Entity 등)
┃ ┣ 📁 domain                 # 도메인 레이어 (UseCase, Repository Interface)
┃ ┣ 📁 data                   # Repository 구현, 데이터 소스
┃ ┣ 📁 database               # Room DB 관련 코드
┃ ┣ 📁 util                   # 공통 유틸리티
┃ ┗ 📁 designsystem           # 디자인 시스템 (UI 컴포넌트, 테마 등)
```

---

## 🛠 주요 구현 내용 (Highlights)


- ✅ **비회원 사용 가능 / 로그인 기능 포함**  
  Firebase Authentication을 통해 비회원인 상태와 로그인 상태 둘다 사용 가능하도록 개발

- ✅ **Firebase + Room 하이브리드 저장 구조**  
  Room DB를 통한 로컬 저장과 Firebase Realtime Database를 통한 실시간 동기화를 함께 구성해 오프라인 환경에서도 정상 동작

- ✅ **저장 애니메이션 제공**  
  Jetpack Compose에서 Lottie 애니메이션과 상태 기반 트리거를 이용해 저장 완료 시  피드백 제공

- ✅ **폴더 기반 클립 정리**  
  클립 데이터를 Folder ID로 구분해 Room + Firebase 구조에 매핑,  폴더별 필터링 제공

- ✅ **클립 편집 및 삭제**  
  각 클립 아이템에 대해 롱 프레스 혹은 아이콘 클릭 이벤트를 통해 편집/삭제 처리
  
- ✅ **공유 폴더 기능 (Firebase 연동)**  
  폴더별 UUID를 기준으로 Firebase에서 해당 폴더의 데이터를  구독, 다른 사용자와 클립을 공유하는 구조로 설계

- ✅ **슬라이드 패널 UI**  
  Jetpack Compose로 오른쪽에서 부드럽게 진입하는 패널 구현



---


## 📌 배운 점 (Lessons Learned)

- 컴포즈를 이용한 MVI 아키텍쳐
- 멀티 모듈화를 통한 기능 확장과 유지보수의 용이성

---

## 📅 향후 개선 사항 (TODO)

- [ ] 게스트 계정 마이그레이션 기능 
- [ ] 태그 기반 클립 관리
- [ ] 웹 사이트 인앱 브라우저로 열기
- [ ] [모듈 구조 개선 관련 이슈 #1](https://github.com/polaris428/Clippy/issues/1)

- [ ] 다국어 지원
- [ ] 테스트 커버리지 강화 (Unit/UI Test)

---

## 👨‍💻 개발자 정보 (About Me)

- 이름: polaris04
- GitHub: [github.com/polaris428](https://github.com/polaris428)


