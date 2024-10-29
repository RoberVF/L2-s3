# MULTICAST
## Funcionamiento
Para ejecutar el programa, escribir en el terminal: 
**Compilar los archivos:**
En la ruta que contiene el src/ de cada uno
```
javac -d src src\emisor\Emisor.java
javac -d src src\receptor\Receptor.java
javac -d src src\receptor\Receptor.java
```

**Ejecutar los archivos:**
En la ruta que contiene el src/ de cada uno
```
java -cp src emisor.Emisor 239.250.0.2 55558 239.250.0.2 55558
java -cp src receptor.Receptor 239.250.0.2 55558  
java -cp src receptor.Receptor 239.250.0.2 55558  
```

Se pueden establecer otras direcciones siempre y cuando sean multicast, incluso direcciones diferentes para el Receptor 1 y el Receptor 2.