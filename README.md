# docker-alfred

![Alfred](https://raw.githubusercontent.com/wuakitv/docker-alfred/master/img/alfred.jpg)

## Generate list of plugins

How to generate a plugins.txt from an existing jenkins instance:

run in https://<your_jenkins_instance_url>/script

```groovy
def plugins = jenkins.model.Jenkins.instance.getPluginManager().getPlugins().toSorted()
plugins.each {println "${it.getShortName()}:${it.getVersion()}"}
```
