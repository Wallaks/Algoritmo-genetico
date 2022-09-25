import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Produto {
    private String nome;
    private Double espaco;
    private Double valor;

    public Produto(String nome, Double espaco, Double valor) {
        this.nome = nome;
        this.espaco = espaco;
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getEspaco() {
        return espaco;
    }

    public void setEspaco(Double espaco) {
        this.espaco = espaco;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }   
}

/*Estou ultilizando o implements para implementar a interface 
Comparable (vou conseguir fazer a comparacao de um individuo com outro)*/
class Individuo implements Comparable<Individuo> {
	
	
    private List espacos = new ArrayList<>(); //espacos que cada item ocupa 
    private List valores = new ArrayList<>(); //precos das coisas
    private Double limiteEspacos; //qual a capaciade maxima do caminhao
    private Double notaAvaliacao; //qual a quantidade total de preco (Do cromossomo)
    private Double espacoUsado; //os metros cubicos que esse individuo estara utilizando
    private int geracao; //identificar a geracao
    private List cromossomo = new ArrayList<>(); //vai estar contida a soluacao do problema
    
   //construtor
    public Individuo(List espacos, List valores, Double limiteEspacos) { 
        this.espacos = espacos;
        this.valores = valores;
        this.limiteEspacos = limiteEspacos;
       
        //inicializacao das variaveis de forma manual
        this.notaAvaliacao = 0.0; 
        this.espacoUsado = 0.0;
        this.geracao = 0;
        
      //Vou popular o arrayList com um for de valores aleatorios
        for (int i = 0; i < this.espacos.size(); i++) { //this.espacos.size = pegando o tamanho da lista de espacos de cada produto (14 produtos)
            if (java.lang.Math.random() < 0.5) { //comecando a inicializacao com 50% de probabilidade (distribuicao igual  pupulando o cromossomo de forma aleatoria)
                this.cromossomo.add("0");    
            } else {
                this.cromossomo.add("1");
            }
        }       
    }
  
    
    
    //Funcao avaliacao (vai pegar os valores que tem 1 e somar o preco e o tamanho ultilizado.)
    public void avaliacao() {//fitness (vou avaliar o cromossomo)
        Double nota = 0.0;
        Double somaEspacos = 0.0;
        for (int i = 0; i < this.cromossomo.size(); i++) {//for para percorrer o cromossomo (cada 0 e cada 1)
            if (this.cromossomo.get(i).equals("1")) {//verificando se ele e igual a um, ou seja, se contem o produto
                nota += (Double) this.valores.get(i);//a nota recebe ela mesma. Concatenando com a lista de valores acima
                somaEspacos += (Double) this.espacos.get(i);//
            }
        }
        
        if (somaEspacos > this.limiteEspacos) {//verificando se nao estourou a capacidade (3)
            nota = 1.0;//ai eu digo que a solucao nao e boa e dou valor 1 pra ela. Dessa forma eu nao vou desperdicar nenum cromossomo
        }
       
        //fazendo a atualizacao dos atributos
        this.notaAvaliacao = nota; //qual a quantidade total de preco (Do cromossomo)
        this.espacoUsado = somaEspacos;//e feita a verificacao do espaco eu tambem atualizo a variavel 
    }
    
   
    
    
    
    //## ESTE METODO ##
    /*combinar pedacos do cromossomo de dois genitores gerando filhos mais aptos (combinacao do primeiro cromossomo com o segundo cromossomo)
    Este crossover e de 1 ponto (O ponto de corte e quantidade de genes -1)*/
    public List crossover(Individuo outroIndividuo) {//recebe como paranmetro um objeto
    	int corte = (int) Math.round(Math.random() * this.cromossomo.size());//retornara o ponto de corte aleatorio
      
        List filho1 = new ArrayList<>();//cromossomo do primeiro filho
        //Codificacao do primeiro filho
        filho1.addAll(outroIndividuo.getCromossomo().subList(0, corte));/*Ultilizado para fazer a concatenacao de uma parte 
        																de um elemento com outra parte de outro elemento. 
        																Se usar somente o add ele vai salvar em linha separada. Dessa forma 
        																eu criei uma sublista onde eu coloco dois parametros, 
        															    de onve vai comecar e ate onde ele vai terminar
        															    como meu cromossomo e de 14 posicoes ele vai pegar da posicao
        															    0 ate a posicao de corte*/
     
        filho1.addAll(this.cromossomo.subList(corte, this.cromossomo.size())); /*Nessa parte eu to fazendo a mesma coisa. Pegando a 
        																	     posicao de corte ate o fim do cromossomo(ultima posicao)*/
       
        
        //Codificacao do segundo filho. Um processo semelhante
        List filho2 = new ArrayList<>();
      
        filho2.addAll(this.cromossomo.subList(0, corte));//faz o mesmo processo que o de cima
        filho2.addAll(outroIndividuo.getCromossomo().subList(corte, this.cromossomo.size()));//mesmo processo 
        
        //Gerou uma lista
        List<Individuo> filhos = new ArrayList<>();
        filhos.add(new Individuo(this.espacos, this.valores, this.limiteEspacos));//estou criando efetivamente um novo individuo e adicionando
        																		  //nessa lista
        filhos.add(new Individuo(this.espacos, this.valores, this.limiteEspacos));
        
        //O que vai diferenciar um individuo para o outro vai ser o cromossomo
        
        filhos.get(0).setCromossomo(filho1);//o primeiro filho que add na lista (efetivamente setando o cromossomo)
        filhos.get(0).setGeracao(this.geracao + 1);//incremento de geracao
       
        //o mesmo processo para a posicao 1
        
        filhos.get(1).setCromossomo(filho2);//setando o filho 2
        filhos.get(1).setGeracao(this.geracao + 1);//concatenando a geracao
       
      
        return filhos;//retorna uma lista com novos filhos gerados 
   
    }
  
    
    
    
    //mutacao cria diversidade mudando aleatoriamente alguns genes e e menos aplicada que a reproducao
   //criacao do metodo passando a taxa como parametro (geralmente baixa) com um retorno de individuo ja mutado
    public Individuo mutacao(Double taxaMutacao) {
      
        for (int i = 0; i < this.cromossomo.size(); i++) {//percorrendo tod 0 e 1
            if (Math.random() < taxaMutacao) {//retona um valor entre 0 e 1 e se for menos que taxaMutacao entao vou fazer a troca dos genes
                if (this.cromossomo.get(i).equals(("1"))) {//se for igual a 1
                    this.cromossomo.set(i, "0");
                } else {//e se for igual a 0 ele muda para 1
                    this.cromossomo.set(i, "1");
                }
            }
        }
       
        return this;//retona o propio objeto
    }

    public Double getEspacoUsado() {
        return espacoUsado;
    }

    public void setEspacoUsado(Double espacoUsado) {
        this.espacoUsado = espacoUsado;
    }
    
    public List getEspacos() {
        return espacos;
    }

    public void setEspacos(List espacos) {
        this.espacos = espacos;
    }

    public List getValores() {
        return valores;
    }

    public void setValores(List valores) {
        this.valores = valores;
    }

    public Double getLimiteEspacos() {
        return limiteEspacos;
    }

    public void setLimiteEspacos(Double limiteEspacos) {
        this.limiteEspacos = limiteEspacos;
    }

    public Double getNotaAvaliacao() {
        return notaAvaliacao;
    }

    public void setNotaAvaliacao(Double notaAvaliacao) {
        this.notaAvaliacao = notaAvaliacao;
    }

    public int getGeracao() {
        return geracao;
    }

    public void setGeracao(int geracao) {
        this.geracao = geracao;
    }

    public List getCromossomo() {
        return cromossomo;
    }

    public void setCromossomo(List cromossomo) {
        this.cromossomo = cromossomo;
    }   

   
    //Ai gera uma implementacao da assinatura
    @Override
    public int compareTo(Individuo o) {//compara um individuo com todos os outros
        if (this.notaAvaliacao > o.getNotaAvaliacao()) {/*se a nota da avaliacao for maior que a nota do 
        												  parametro ele retorna -1 e o individuo ficara na frete
        												  dessa forma eu trabalho em ordem decrecente. Eu quero colocar na primeira posicao o 
        											      individuo com a maior nota */
            return -1;
        }
      //aqui eu faco o contrario
        if (this.notaAvaliacao < o.getNotaAvaliacao()) {
            return 1;
        }
        return 0;
    }
}

//Aqui tem todas as funcoes juntas para a resolucao do problema 
class AlgoritmoGenetico {
    private int tamanhoPopulacao; //aqui eu vou dizer quantos individuos irao fazer parte da minha populacao
    private List<Individuo> populacao = new ArrayList<>();//vou armazenar nessa lista todos os individuos que fazem parte da minha populacao
    private int geracao;//vai dizer qual a geracao do AG
    private Individuo melhorSolucao;//atributo para armazenar qual e o que obteve melhor solucao

    //Quando criar um AG eu vou falar quantos individuos farao parte da minha populacao
    public AlgoritmoGenetico(int tamanhoPopulacao) {
        this.tamanhoPopulacao = tamanhoPopulacao;
    }
    
  
    
    
    //Metodo para criar inicializar uma populacao com seus respectivos parametros
    public void inicializaPopulacao(List espacos, List valores, Double limiteEspacos) {
        for (int i = 0; i < this.tamanhoPopulacao; i++) {//vou executar o for de acordo com o parametro passado acima que o tamPopulacao
            this.populacao.add(new Individuo(espacos, valores, limiteEspacos));//vou chamar o add e criando um novo individuo com tais atributos
        }
        this.melhorSolucao = this.populacao.get(0);//eu escolhi de forma aleatoria qual o melhor individuo dizendo que o da posicao 0 seria o melhor
    }
  
    
    
    //Metodo para ordenar populacao
    public void ordenaPopulacao() {
      
    	//Collections traz um m�todo est�tico sort que recebe um List como argumento e o ordena por ordem crescente.
    	Collections.sort(this.populacao);//dessa maneira eu utilizo o metodo criado anteriormente e ira fazer a ordenacao
    }
    
 
    
    
    
    //metodo para saber qual e o melhor individuo
    public void melhorIndividuo(Individuo individuo) {
        if (individuo.getNotaAvaliacao() > this.melhorSolucao.getNotaAvaliacao()) {//Se o individuo for maior this.melhorSolucao.getNotaAvaliacao
        																			//entao eu faco a troca dos individuos
            this.melhorSolucao = individuo; //melhor solucao parcial
        }
    }
    
   
    
    
    
    
    
    //Pegar todos os valores que forem retornados de cada individuo da populacao e fazermos um simples somatorio
    public Double somaAvaliacoes() {
        Double soma = 0.0;
        for (Individuo individuo: this.populacao) {//percorrendo todos os individuos de nossa populacao
            soma += individuo.getNotaAvaliacao();//e vou soamndo dentro dessa variavel soma para depois retornar ela
        }
        return soma;
       //Esse metodo esta fazendo a soma em reais de toda a populacao para mais tarde eu ultilizar o metodo de roleta
    }
    
    
    
    
    
    
    // Vou passar como parametro o retorno do metodo acima e vai retornar um int que vai 
    //indicar em qua posicao da lista estara o elemento selecionado
   
    //## aqui e o metodo roleta ##
    public int selecionaPai(Double somaAvaliacao) {
      
    	int pai = -1;//quer dizer que nao vou retornar nada pelo fato de ser uma posicao invalida
      //aqui acontece o sorteio. Verifica-se o somatorio das avaliacoes e faz a multiplicacao gerandor um valor aleatorio
        Double valorSorteado = Math.random() * somaAvaliacao;//()random valor entre 0 e 1 e o somaAvaliacao e um valor entre EX: 0 a 200 mil reais
        Double soma = 0.0;
        int i = 0;//vai ser ultilizada para popularmos o loop
        while (i < this.populacao.size() && soma < valorSorteado) {//nao vou poder executar o loop caso ele passe do tamanho da populacao
        															//e o valor da soma tem que ser menor que o valor sorteado
            
        	soma += this.populacao.get(i).getNotaAvaliacao();//incrementando pegando o elemento que estou percorrendo e buscando a 
           
            pai += 1; //incrementando o pai. Esse pai vai pegar em qual posicao da minha lista esta o elemento selecionado (quando parar 
            			//de executar tera na variavel a posicao EX/ 1, 4)
          
            i += 1;//incremento de variavel
        }

        return pai;//efetivamente encontrou a posicao
       
    }
  
  
    
    //print para acompanhar como estao os resultados
    public void visualizaGeracao() {
      
    	Individuo melhor = this.populacao.get(0);//aqui eu pego a posicao 0 porque eu parto do ponto que eu ja fiz a ordenacao
       
        //um print onde g significa a geracao, valor, etc
        System.out.println("G: " + melhor.getGeracao() +
                "\n Valor: " + melhor.getNotaAvaliacao() +
                "\n Espa�o: " + melhor.getEspacoUsado() +
                "\n Cromossomo: " + melhor.getCromossomo());
    }
  
    
    
    // ### METODO FAZ TUDO ###
    //Aqui vamos retornar o cromossomo
    public List resolver(Double taxaMutacao, int numeroGeracoes, List espacos, 
            List valores, Double limiteEspacos) {
        
    	//vou inicializar a populacao
        this.inicializaPopulacao(espacos, valores, limiteEspacos);
     
        //aquio vou fazer a avaliacao
        for (Individuo individuo: this.populacao) {
        	individuo.avaliacao();
        }
       
        this.ordenaPopulacao();  //aqui eu chamo o metodo de ordena populacao
    
        
        this.visualizaGeracao();  //imprimo o resultado
        
        //aqui eu vou defininir um criterio de parada que no meu caso e por numero de geracao
        for (int geracao = 0; geracao < numeroGeracoes; geracao++) {//inicializacao do for 
            Double somaAvaliacao = this.somaAvaliacoes();//vai pegar toda a populacao para fazer o somatorio
            List<Individuo> novaPopulacao = new ArrayList<>();//instancia de uma lista de nova populacao
            
            //aqui 
            for (int i = 0; i < this.populacao.size() / 2; i++) {//percorrimento de cada um dos elementos da populacao dividido por 2 (porque ele faz a combinacao de dois individuos
            													 //e vai gerar o dobro e vai estourar a acapacidade do tamanho da populacao)
            													//dai eu executo o for a metade de vezes gerando dois filhos novos
                
            	int pai1 = this.selecionaPai(somaAvaliacao);//esse for vai rodar, por exemplo, 10 vezes e vai pegar o pai 1 e o pai 2
                int pai2 = this.selecionaPai(somaAvaliacao);//
                
                
                //criando uma lista de individuos.
                List<Individuo> filhos = this.getPopulacao().get(pai1).crossover(this.getPopulacao().get(pai2));//aqui faz o crossover dp pai 1 com pai 2               																								
                
                //entao eu pego os filhos geraddos e aplico a mutacao e adiciono eles na lista
                novaPopulacao.add(filhos.get(0).mutacao(taxaMutacao));
                novaPopulacao.add(filhos.get(1).mutacao(taxaMutacao));
            }
            
            //aqui vou setar na nova populacao jogando a populacao anterior no lixo
            this.setPopulacao(novaPopulacao);
           
            //for para avaliar individuo
            for (Individuo individuo: this.getPopulacao()) {
             //aqui chamando o metodo avaliacao
            	individuo.avaliacao();
            }
          
            
            //aqui vou ordenar a populacao
            this.ordenaPopulacao();
           //aqui vou visualizar 
           this.visualizaGeracao();
           
           //na posicao 0 da lista eu tenho um individuo com a melhor nota
           Individuo melhor = this.populacao.get(0);
          //aqui eu vou atualizar o atributo
           this.melhorIndividuo(melhor);
            
        }
       //mostrado resultados
        System.out.println("##### Melhor solu��o G -> " + this.melhorSolucao.getGeracao() +" #####" +
                "\n Valor: " + this.melhorSolucao.getNotaAvaliacao() +
                "\n Espa�o: " + this.melhorSolucao.getEspacoUsado() + 
                "\n Cromossomo: " + this.melhorSolucao.getCromossomo()+"\n");
        return this.melhorSolucao.getCromossomo();
    }

    //geter e seters para pegar valores e atualizar valores da populacao
    public int getTamanhoPopulacao() {
        return tamanhoPopulacao;
    }

    public void setTamanhoPopulacao(int tamanhoPopulacao) {
        this.tamanhoPopulacao = tamanhoPopulacao;
    }

    public List<Individuo> getPopulacao() {
        return populacao;
    }

    public void setPopulacao(List<Individuo> populacao) {
        this.populacao = populacao;
    }

    public int getGeracao() {
        return geracao;
    }

    public void setGeracao(int geracao) {
        this.geracao = geracao;
    }

    public Individuo getMelhorSolucao() {
        return melhorSolucao;
    }

    public void setMelhorSolucao(Individuo melhorSolucao) {
        this.melhorSolucao = melhorSolucao;
    }    
}

public class Executar {
    public static void main(String args[]) {
        List<Produto> listaProdutos = new ArrayList<>();
        listaProdutos.add(new Produto("Geladeira Dako", 0.751, 999.90));
        listaProdutos.add(new Produto("Iphone 6", 0.000089, 2911.12));
        listaProdutos.add(new Produto("TV 55' ", 0.400, 4346.99));
        listaProdutos.add(new Produto("TV 50' ", 0.290, 3999.90));
        listaProdutos.add(new Produto("TV 42' ", 0.200, 2999.00));
        listaProdutos.add(new Produto("Notebook Dell", 0.00350, 2499.90));
        listaProdutos.add(new Produto("Ventilador Panasonic", 0.496, 199.90));
        listaProdutos.add(new Produto("Microondas Electrolux", 0.0424, 308.66));
        listaProdutos.add(new Produto("Microondas LG", 0.0544, 429.90));
        listaProdutos.add(new Produto("Microondas Panasonic", 0.0319, 299.29));
        listaProdutos.add(new Produto("Geladeira Brastemp", 0.635, 849.00));
        listaProdutos.add(new Produto("Geladeira Consul", 0.870, 1199.89));
        listaProdutos.add(new Produto("Notebook Lenovo", 0.498, 1999.90));
        listaProdutos.add(new Produto("Notebook Asus", 0.527, 3999.00));
        
        //cada uma das tres lista irao armazenar esses valores de cima
        List espacos = new ArrayList<>(); //
        List valores = new ArrayList<>(); //
        List nomes = new ArrayList<>();  //
   
        //popular a lista
        for (Produto produto: listaProdutos) {//vou percorrer a variavel listaProduto
           //adicionando
        	espacos.add(produto.getEspaco());
            valores.add(produto.getValor());
            nomes.add(produto.getNome());
        }
        Double limite = 3.0; //limite do espaco no bau do caminhao
        int tamanhoPopulacao = 3; //uma populacao de x individuos
        Double taxaMutacao = 0.05; //uma taxa de mutacao de x%
        int numeroGeracoes = 4;//um numero de geracoes
        AlgoritmoGenetico ag = new AlgoritmoGenetico(tamanhoPopulacao);
        //aqui vou efetivamente retornar o melhor cromossomo
        List resultado = ag.resolver(taxaMutacao, numeroGeracoes, espacos, valores, limite);//
       
        //esse for serve para mostrar os produtos que o caminhao vai levar
        for (int i = 0; i < listaProdutos.size(); i++) {//percorrendo a alista de produtos
            if (resultado.get(i).equals("1")) {//ai eu digo que se o resultado for 1 eu mostro ele
                System.out.println("Nome: " + listaProdutos.get(i).getNome());
            }
        }
    }
}
