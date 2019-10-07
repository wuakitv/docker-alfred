# docker-alfred

![Alfred](https://raw.githubusercontent.com/wuakitv/docker-alfred/master/img/alfred.jpg)

## Updating list of plugins

After generating a new image, the new step is to change the image tag in Puppet. Before you apply this changes via puppet, make sure you have deleted the old/current plugins. Connect to Alfred via SSH, and execute the following command:

```
rm -rf /var/jenkins_home/plugins/*
```

## Generate list of plugins

How to generate a plugins.txt from an existing jenkins instance:

run in https://<your_jenkins_instance_url>/script

```groovy
def plugins = jenkins.model.Jenkins.instance.getPluginManager().getPlugins().toSorted()
plugins.each {println "${it.getShortName()}:${it.getVersion()}"}
```
