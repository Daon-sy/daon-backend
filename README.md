# Daon Service Backend

## Project Guide

### Clone Repository
```shell
$ git clone --recurse-submodules https://github.com/Daon-sy/daon-backend.git
# go to project-root
$ cd daon-backend
```

### Update submodule repository
- 모든 서브모듈을 최신 버전으로 업데이트
```shell
$ git submodule update --remote
```

### Commit & push submodule repository
- 아래 명령을 통해 submodule repository 내용 변경
```shell
$ git submodule update --remote
# do this command at project-root path
$ cd src/main/resources/config
$ git checkout main
$ git add .
$ git commit -m "some messages"
$ git push -u origin main
# go to project-root
$ cd ../../../..
```

- submodule 변경 내역을 main-repository에도 반영해줘야 한다.
```shell
# do this command at project-root path
$ git add src/main/resources/config
$ git commit -m "some messages"
$ git push -u origin [branch]
```
