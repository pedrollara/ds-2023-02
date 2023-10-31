interface Expressao {
    float valor();
}

class Constante implements Expressao {
    private float valor;
    public Constante(float valor) {
        this.valor = valor;
    }

    @Override
    public float valor() {
        return valor;
    }
}

class Multiplica implements Expressao {
    private Expressao expressao1;
    private Expressao expressao2;
    public Multiplica(Expressao expressao1, Expressao expressao2) {
        this.expressao1 = expressao1;this.expressao2 = expressao2;
    }

    @Override
    public float valor() {
        return expressao1.valor() * expressao2.valor();
    }
}

class Soma implements Expressao {
    private Expressao expressao1;
    private Expressao expressao2;

    public Soma(Expressao expressao1, Expressao expressao2) {
        this.expressao1 = expressao1;this.expressao2 = expressao2;
    }

    @Override
    public float valor() {
        return expressao1.valor() + expressao2.valor();
    }
}

class Subtrai implements Expressao {
    private Expressao expressao1;
    private Expressao expressao2;
    public Subtrai(Expressao expressao1, Expressao expressao2) {
        this.expressao1 = expressao1;this.expressao2 = expressao2;
    }
    @Override
    public float valor() {
        return expressao1.valor() - expressao2.valor();
    }
}

class Divide implements Expressao {
    private Expressao expressao1;
    private Expressao expressao2;
    public Divide(Expressao expressao1, Expressao expressao2) {
        this.expressao1 = expressao1;
        this.expressao2 = expressao2;
    }
    @Override
    public float valor() {
        return expressao1.valor() / expressao2.valor();
    }
}

public class Main {
    public static void main(String[] args) {
        Expressao expressao = new Soma(new Constante(5), new Multiplica(new Constante(2), new Constante(3)));
        float resultado = expressao.valor();
        System.out.println("Resultado da expressão: " + resultado);
    }
}
