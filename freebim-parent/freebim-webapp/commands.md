## The Configuration Directory

```
cd /etc

# creating directory
sudo mkdir -p asi-propertyserver/production
sudo mkdir -p asi-propertyserver/backup
cd freebim

# change group and rights for group
sudo chown root:_developer production/
sudo chown root:_developer backup/
sudo chmod g+w production
sudo chmod g+w backup

```
