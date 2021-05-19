
## 슬랙 채팅 봇
최초 Github commit 여부를 확인해주는 개인 프로젝트로 시작하였습니다. 
Team Geneuin 업무를 돕기 위한 SLACK 채팅 봇입니다. 
추가되는 기능들에 

### 단순 Commit 여부 확인 기능, 2021-04-08
- AWS Lambda 활용
- Slack 토큰 정보, 사용자 ID 전달
- 해당 사용자의 1 Day 1 Commit 여부 확인 후 Slack 메세지 전달

### 1 Day 1 Commit 채팅방 지정 후 해당 채팅방의 사용자들의 Push 여부 확인, 2021-05-18
- AWS Lambda 활용
- Slack 토큰 정보, 1 Day 1 Commit 채팅방 이름 전달

##### 기타 추가할 기능
- webhook 연동
- 영수증 사진 올릴 시 회사 이메일로 영수증 전송, webhook 키워드, #영수증