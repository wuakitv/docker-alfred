FROM jenkins/jenkins:2.89.2

ENV DOCKER_COMPOSE_VERSION 1.18.0

USER root
RUN curl -L https://github.com/docker/compose/releases/download/${DOCKER_COMPOSE_VERSION}/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
RUN chmod +x /usr/local/bin/docker-compose

RUN install-plugins.sh github-oauth pipeline-multibranch-defaults ec2 ansicolor

USER jenkins