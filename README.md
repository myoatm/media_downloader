Media Downloader
====================


 Media Downloader는  facebook, instagram, youtube 그 외 다양한 플랫폼 및 서비스들의 미디어를 다운받을 수 있도록 하는 솔루션 개발 프로젝트입니다.

 ## Overview
 #### 미디어 게시물의 주소(URL)를 이용하여 문서를 가공 혹은 파싱하여 미디어 자체의 주소를 추출합니다.
 #### 추출한 URL을 통해 사용자 로컬로 다운로드를 할 수 있도록 지원합니다.
 ![인스타그램의 게시물 URL을 통해 이미지 다운로드](/ref_img/p1_overview.png)

## Feature
1) 다양한 서비스를 지원하기 위해 각 서비스 프로바이더들의 데이터를 정규화하고, 사용자에게는 간단한 View를 제공하기 위해 SPA를 추구함
2) 섬네일 제공 및 일정 시간 이후 TGC(Thumbnail Garbage Collector)에 의해 서버에서 삭제
3) 비공개 계정 혹은 게시물에서 접근 권한이 있을 경우 소스를 입력하여 미디어를 다운로드 할 수 있도록 지원

## Support Services
> Instagram
 ### > Instagram Single/Multi Image
 ### > Instagram Video
&nbsp;
 >Facebook
 ### > Facebook Single/Multi Image
 ### > Facebook Video
&nbsp;
 >Youtube
 ### > Youtube Video
 ### > Youtube Playlist
&nbsp;
&nbsp;
 ## Issue/Trouble/Improvements
1. ~~~Thumbnail(File) Garbage Collector에 다수 요청으로 인한 퍼포먼스 문제~~~
&nbsp;
2. Youtube Video/Playlist 항목 구현
&nbsp;
3. 단축URL 문제