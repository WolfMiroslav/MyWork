
package ea_mala_prezentacia_1;

import java.util.ArrayList;
import java.util.Random;
import javax.naming.BinaryRefAddr;

/**
 *
 * Evolucne Algoritmy - mala prezentacia 1
 * 
 * Zadanie 3:
 * Majte 10 kariet očíslovaných od 1 do 10. Zvoľte si metódu, ako ich rozdeliť do dvoch
 * kôp tak, aby súčet hodnôt v prvej kôpke bol čo možno najbližší 36 a súčin hodnôt druhej
 * kôpky čo možno najbližší 360. Navrhnite kódovanie genotypu, ktoré bude kódovať
 * riešenie (možných riešení je 1024). Navrhnite funkciu fitnes, ktorá bude merať, ako ďaleko
 * je riešenie od ideálneho. Skúste napísať či ukradnúť GA program, upraviť na Vaše
 * kódovanie a funkciu, zbehnúť stokrát a ukázať grafy závislosti najlepšieho a priemerného
 * fitnes na generácii (uveďte použitý typ mutácie a kríženia a selekcie).
 *
 * @author bc. Miroslav Wolf
 * FIIT STU ©2015
 * 
 */
public class EA_mala_prezentacia_1 {

    /**
     * Binary representation of random splitted cards 
     * 
     * @return binary string of splitted cards
     */
    public static String splitCards(){ 
        Random r = new Random();
        int rand = 1 + r.nextInt(1023);                
        return Integer.toBinaryString(rand);        
    }
    
    /**
     * Card calculations - sum of cards in first pack and multiply in second pack
     * 
     * @param subj
     * @return subject with calculated cards
     * 
     */
    public static Subject calc(Subject subj){        
        subj.setId(subj.getId());
        subj.setSplit(subj.getSplit());                
        int sumCards = 0;
        int multiplyCards = 0;
        for(int j=0;j<subj.getSplit().length();j++){
            if(subj.getSplit().charAt(j) == '0'){
                sumCards = sumCards + (j+1);
            }
            else if (subj.getSplit().charAt(j) == '1'){
                if(multiplyCards == 0) {
                    multiplyCards = (j+1);
                }
                else{
                    multiplyCards = multiplyCards * (j+1);
                }
            }
            else {
                System.out.println("ERROR: TOO MANY CARDS");
            }
        }
        
        subj.setSum(sumCards);
        subj.setMultiply(multiplyCards);                
        subj.setDifference(Math.abs((Math.abs(36.0 - sumCards)) + (Math.abs(360.0 - multiplyCards))));
        if(subj.getDifference()!=0){
            subj.setFitness(100000.0 / subj.getDifference());
        }
        else {
            subj.setFitness(100010.0);
        }
        
        return subj;
    }
    
    /**
     * Generating of new subject
     * 
     * @param id
     * @return new generated subject
     * 
     */
    public static Subject generateSubject(int id) {         
        Subject subj = new Subject();                    
        String split = splitCards();        
        subj.setId(id);
        
        while(split.length()<10){
            split = "0"+split;
        }        
        subj.setSplit(split);
        
        return calc(subj);        
    }
    
    /**
     * Mutation of subjects 
     * 
     * @param subj
     * @return mutated subject
     * 
     */
    public static Subject mutation(Subject subj) {
        
        Random rand = new Random();
        String gene = new String();
        String mutateGene = new String();
        double oldDiff = subj.getDifference();
       
        Subject pom = new Subject();
        pom.setId(subj.getId());
        pom.setDifference(subj.getDifference());
        pom.setFitness(subj.getFitness());
        pom.setMultiply(subj.getMultiply());
        pom.setSum(subj.getSum());
        pom.setSplit(subj.getSplit());
        
       
        for(int i=0;i<subj.getSplit().length();i++){
            if(rand.nextDouble() < 0.05){                                               
                if(subj.getSplit().substring(i, i+1).equals("0")){
                    mutateGene = "1";
                }
                else if(subj.getSplit().substring(i, i+1).equals("1")){
                    mutateGene = "0";
                }
                else{
                    System.out.println("WARNING: WRONG FORMAT OF GENE");
                }
             
                String fin = subj.getSplit().substring(0,i)+mutateGene+subj.getSplit().substring(i+1);
                if(fin.equals("0000000000") == false || fin.equals("1111111111") == false){
                    subj.setSplit(subj.getSplit().substring(0,i)+mutateGene+subj.getSplit().substring(i+1));
                }
            }            
        }
        subj = calc(subj);
        if(oldDiff < subj.getDifference()){
            return pom;
        }
        else {
            return subj;
        }        
    }
    
    public static void main(String[] args) {
        ArrayList<Double> average = new ArrayList<Double>();
        ArrayList<Double> popbest = new ArrayList<Double>();
        ArrayList<Double> worse = new ArrayList<Double>();
        int populationSize = 6;
        int nrOfGenerations = 100;
        ArrayList<Subject> population = new ArrayList<Subject>();
        ArrayList<Subject> newPopulation = new ArrayList<Subject>();
        
        for(int i=0;i<populationSize;i++){
            population.add(generateSubject(i));            
        }
        
        for (int i=0;i<nrOfGenerations;i++){
            double allFitness = 0;
            for(int j=0; j<populationSize;j++){
                allFitness += population.get(j).getFitness();
            }
            
            Subject best = new Subject();
            best.setId(population.get(0).getId());
            best.setDifference(population.get(0).getDifference());
            best.setFitness(population.get(0).getFitness());
            best.setMultiply(population.get(0).getMultiply());
            best.setSum(population.get(0).getSum());
            best.setSplit(population.get(0).getSplit());
            for(int j=0;j<populationSize;j++){
                if(population.get(j).getFitness() > best.getFitness()){
                    best = new Subject();
                    best.setId(population.get(j).getId());
                    best.setDifference(population.get(j).getDifference());
                    best.setFitness(population.get(j).getFitness());
                    best.setMultiply(population.get(j).getMultiply());
                    best.setSum(population.get(j).getSum());
                    best.setSplit(population.get(j).getSplit());
                }
            }           
            popbest.add(best.getFitness());
            newPopulation.add(best);
            
            int j=0;            
            double avg = 0;
            double pom = 0;
            
            
            /**
             * Roulette wheel selection
             * 
             */
            while(newPopulation.size() != populationSize){               
                
                avg += (double)population.get(j).getFitness() / allFitness;
                Random rand = new Random();
                double r = rand.nextDouble();                
                
                if(pom < r && r < avg){
                    Subject subj = new Subject();
                    subj.setId(population.get(j).getId());
                    subj.setDifference(population.get(j).getDifference());
                    subj.setFitness(population.get(j).getFitness());
                    subj.setMultiply(population.get(j).getMultiply());
                    subj.setSum(population.get(j).getSum());
                    subj.setSplit(population.get(j).getSplit());
                    newPopulation.add(calc(mutation(subj))); 
                    
                    j=0;
                    avg = 0;
                    pom = 0;
                    continue;
                }
                
                pom = avg;                
                
                if(j==populationSize-1) {
                    j=0;
                    avg = 0;
                    pom = 0;
                }
                else {
                    j++;
                }
            }
            
            population.clear();
            population = new ArrayList<>();
            
            double avgFit = 0.0;
            for(j=0;j<populationSize;j++){
                Subject subj = new Subject();
                subj.setId(newPopulation.get(j).getId());
                subj.setDifference(newPopulation.get(j).getDifference());
                subj.setFitness(newPopulation.get(j).getFitness());
                subj.setMultiply(newPopulation.get(j).getMultiply());
                subj.setSum(newPopulation.get(j).getSum());
                subj.setSplit(newPopulation.get(j).getSplit());

                avgFit += subj.getFitness();
                population.add(subj);                
            } 
            avgFit = avgFit / populationSize;            
            average.add(avgFit);
            avgFit = 0;
            
            newPopulation.clear();
            newPopulation = new ArrayList<>();           
        }
        System.out.println(population.get(0).getSplit());
        System.out.println(population.get(0).getDifference());  
    }
}
