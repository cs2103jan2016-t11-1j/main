all:
	javac TidyYard.java

run: TidyYard.class
	java TidyYard test.txt
clean:
	rm test.txt *.class

test:
	javac Tests.java
	java org.junit.runner.JUnitCore Tests
