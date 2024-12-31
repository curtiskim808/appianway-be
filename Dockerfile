FROM ubuntu:latest
LABEL authors="curtiskim"

ENTRYPOINT ["top", "-b"]