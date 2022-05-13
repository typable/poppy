# minecraft-hub
A Minecraft Server Management Plugin

### Authentication

```bash
ssh-keygen // no password
eval $(ssh-agent)
ssh-add ~/.ssh/id_rsa
scp ~/.ssh/id_rsa.pub <user>@<host>:~/.ssh/authorized_keys
```

More information: [Passwordless-SSH-Login](https://endjin.com/blog/2019/09/passwordless-ssh-from-windows-10-to-raspberry-pi)

### Export as Jar File

```bash
gradle build
```

### Transfer to Local Server

Custom configuration required!

```bash
gradle task local
```

### Transfer to Remote Server

```bash
gradle task deploy
```
