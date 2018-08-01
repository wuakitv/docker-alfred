FROM jenkins/jenkins:2.135

ENV DOCKER_COMPOSE_VERSION 1.21.1

USER root
RUN curl -L https://github.com/docker/compose/releases/download/${DOCKER_COMPOSE_VERSION}/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
RUN chmod +x /usr/local/bin/docker-compose

# how to generate a plugins.txt from an existing jenkins instance
# in /script
#
#def plugins = jenkins.model.Jenkins.instance.getPluginManager().getPlugins().toSorted()
#plugins.each {println "${it.getShortName()}: ${it.getVersion()}"}

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh <  /usr/share/jenkins/ref/plugins.txt

RUN echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state
RUN echo 2.0 > /usr/share/jenkins/ref/jenkins.install.InstallUtil.lastExecVersion

USER jenkins
