import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java Main <nome da classe> <dir> [-p]");
            System.exit(1);
        }

        String nomeDaClasse = args[0];
        String diretorio = args[1];
        boolean isParalelo = args.length > 2 && args[2].equals("-p");

        try {
            long tempoInicial = System.currentTimeMillis();

            List<String> arquivos = new ArrayList<>();
            Path directory = Path.of(diretorio);

            Stream<Path> filesStream = isParalelo ? Files.walk(directory).parallel() : Files.walk(directory);
            filesStream.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    if (filePath.toString().endsWith(".class") || filePath.toString().endsWith(".jar") || filePath.toString().endsWith(".war")) {
                        if (verificarSeTemNaClasse(filePath, nomeDaClasse)) {
                            arquivos.add(filePath.toString());
                        }
                    }
                }
            });

        Comparator<String> customComparator = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                int number1 = extractNumber(s1);
                int number2 = extractNumber(s2);
                return Integer.compare(number1, number2);
            }
        };

        arquivos.sort(customComparator);
            

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - tempoInicial;

            if (arquivos.isEmpty()) {
                System.out.println("A classe não foi encontrada.");
            } else {
                System.out.println("A classe foi encontrada nos seguintes locais:");
                int i = -3;
                for (String arquivo : arquivos) {
                    i++;
                    System.out.println(i + " - " + arquivo);
                }
            }

            System.out.println("Tempo gasto: " + elapsedTime + " ms");
            System.out.println("Número de arquivos .class .jar e .war em a classe" + nomeDaClasse + " foi encontrada: " + arquivos.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static int extractNumber(String str) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 0;
    }

private static boolean verificarSeTemNaClasse(Path filePath, String nomeDaClasse) {
    if (filePath.toString().endsWith(".class") || filePath.toString().endsWith(".jar") || filePath.toString().endsWith(".war")) {
        try {
            byte[] classBytes = Files.readAllBytes(filePath);
            String classContent = new String(classBytes, "ISO-8859-1"); 
            if (classContent.contains(nomeDaClasse)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return false;
}

}

