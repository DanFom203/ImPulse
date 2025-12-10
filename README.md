# ImPulse – демо веб-приложение

## Описание
Это учебное веб-приложение, состоящее из frontend (на Vue.js) и backend (Spring Boot).
Подключена кластерная БД PostgreSQL с репликацией через haproxy.
В систему входит стек мониторинга и логирования: Promtail, Prometheus, Loki, Grafana.

- [Документация ImPulse](https://docs.google.com/document/d/1d7-dOYGisRbLevdGnCuJMgfgFIVJyBu2Li3JAz_cuSo/edit?usp=sharing)

## Запуск
Осуществлялся запуск на JDK21, в Git Bash терминале Windows 11.

### ВАЖНО!
image: docker.io/bitnami/postgresql-repmgr:14.9.0 стал платным, и сборка проекта с репликацией баз данных (master-slave) теперь недоступна бесплатно. Мы работаем над версией проекта без репликации в отдельной ветке develop-hotfix-no-replication.

### Сборка (в ветке develop)
./build.sh -t v1.0
![build_cmd.png](imgs/build_cmd.png)

### Запуск (в ветке develop)
./run.sh -t v1.0
![run.png](imgs/run.png)

## Результаты:
![grafana.png](imgs/grafana.png)
![docker_images.png](imgs/docker_images.png)
![docker_containers.png](imgs/docker_containers.png)
![prometheus.png](imgs/prometheus.png)
![promtail.png](imgs/promtail.png)
