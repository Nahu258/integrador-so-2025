bash
time java -jar circuito.jar
top -b -d 2
> metricas_rendimiento.txt &
ps aux
--sort=-%cpu > procesos_cpu.txt
free -m >
memoria_uso.txt