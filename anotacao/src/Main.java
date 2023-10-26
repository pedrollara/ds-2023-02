import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Observer {
    String observado();
}

class Observable {
    private String identificador;
    private Map<String, List<Method>> observers = new HashMap<>();

    public Observable(String identificador) {
        this.identificador = identificador;
    }

    public void addObserver(Object observer) {
        Class<?> observerClass = observer.getClass();
        Method[] methods = observerClass.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Observer.class)) {
                String observado = method.getAnnotation(Observer.class).observado();
                observers.computeIfAbsent(observado, k -> new ArrayList<>()).add(method);
            }
        }
    }


    public void removeObserver(Object observer) {
        Class<?> observerClass = observer.getClass();
        Method[] methods = observerClass.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Observer.class)) {
                String observado = method.getAnnotation(Observer.class).observado();
                if (observers.containsKey(observado)) {
                    observers.get(observado).remove(method);
                }
            }
        }
    }

    public void notifyObservers(String observado, String mensagem) {
        observers.getOrDefault(observado, new ArrayList<>()).forEach(method -> {
            try {
                method.invoke(method.getDeclaringClass().newInstance(), mensagem);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }


    public void evento(String mensagem) {
        notifyObservers(identificador, mensagem);
    }
}

public class Main {
    public static void main(String[] args) {
        Observable carro = new Observable("carro");
        Observable aviao = new Observable("aviao");

        CarroObserver carroObserver = new CarroObserver();
        AviaoObserver aviaoObserver = new AviaoObserver();
        EstradaObserver estradaObserver = new EstradaObserver();

        carro.addObserver(carroObserver);
        carro.addObserver(estradaObserver);
        aviao.addObserver(estradaObserver);
        aviao.addObserver(aviaoObserver);

        carro.evento("O carro está se movendo.");
        aviao.evento("O avião está voando.");
    }
}

class CarroObserver {
    @Observer(observado = "carro")
    public void update(String mensagem) {
        System.out.println("Carro recebeu uma mensagem: " + mensagem);
    }
}

class EstradaObserver{
    @Observer(observado = "carro")
    public void update(String mensagem){
        System.out.println("A estrada ouviu um evento: " + mensagem);
    }
}

class AviaoObserver {
    @Observer(observado = "aviao")
    public void update(String mensagem) {
        System.out.println("Avião recebeu uma mensagem: " + mensagem);
    }
}
