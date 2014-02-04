package com.mrprez.gencross.impl.tdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.history.ProportionalHistoryFactory;
import com.mrprez.gencross.value.IntValue;
import com.mrprez.gencross.value.Value;

public class TroneDeFer extends Personnage {
	
	
	
	
	@Override
	public void calculate() {
		super.calculate();
		if(phase.equals("Age")){
			if(getProperty("Age").getValue().getString().isEmpty()){
				errors.add("How old is your character?");
			}
		}else if(phase.equals("Flaws and Drawbacks")){
			calculateDrawbackAndFlaw();
		}
	}
	
	public void calculateDrawbackAndFlaw(){
		int mandatoryFlawsCount = 0;
		for(Property ability : getProperty("Abilities").getSubProperties()){
			if(getCompulsoryFlaw().contains(ability.getName())){
				mandatoryFlawsCount = mandatoryFlawsCount + ability.getMax().getInt() - ability.getValue().getInt();
			}
		}
		if(mandatoryFlawsCount < getFlawNumber()){
			errors.add("A flaw for any "+getFlawNumber()+" of the abilities "+getCompulsoryFlaw());
		}
	}

	public boolean changeAgeValue(Property age, Value newValue){
		try{
			if(newValue.getInt()<0){
				actionMessage = "Only positive integer allowed";
				return false;
			}
		}catch(NumberFormatException nfe){
			actionMessage = "Only positive integer allowed";
			return false;
		}
		return true;
	}
	
	public boolean changeAbility(Property ability, Value newValue){
		for(Property speciality : ability.getSubProperties()){
			if(speciality.getValue().getInt() > newValue.getInt()){
				actionMessage = "Ability cannot be under speciality level";
				return false;
			}
		}
		return true;
	}
	
	public boolean changeSpeciality(Property speciality, Value newValue){
		Property ability = (Property) speciality.getOwner();
		if(newValue.getInt() > ability.getValue().getInt()){
			actionMessage = "Speciality cannot exceed ability";
			return false;
		}
		return true;
	}
	
	public void endAgePhase(){
		int age = getProperty("Age").getValue().getInt();
		int caracPoints;
		int spePoints;
		int maxRank;
		if(age<=9){
			caracPoints = 120;
			spePoints = 40;
			maxRank = 4;
		}else if(age<=13){
			caracPoints = 150;
			spePoints = 40;
			maxRank = 4;
		}else if(age<=18){
			caracPoints = 180;
			spePoints = 60;
			maxRank = 5;
		}else if(age<=30){
			caracPoints = 210;
			spePoints = 80;
			maxRank = 7;
		}else if(age<=50){
			caracPoints = 240;
			spePoints = 100;
			maxRank = 6;
		}else if(age<=70){
			caracPoints = 270;
			spePoints = 160;
			maxRank = 5;
		}else if(age<=80){
			caracPoints = 330;
			spePoints = 200;
			maxRank = 5;
		}else{
			caracPoints = 360;
			spePoints = 240;
			maxRank = 5;
		}
		getPointPools().get("Abilities").add(caracPoints);
		getPointPools().get("Specialities").add(spePoints);
		for(Property carac : getProperty("Abilities").getSubProperties()){
			carac.setMax(new IntValue(maxRank));
			carac.setEditable(true);
		}
	}
	
	public void startDrawbackAndFlaw(){
		getPointPools().get("Flaws and Drawbacks").add(getDrawbackOrFlawNumber());
		for(Property ability : getProperty("Abilities").getSubProperties()){
			ability.setMax();
			for(Property speciality : ability.getSubProperties()){
				speciality.setEditable(false);
			}
			ability.getSubProperties().setFixe(true);
			ability.setHistoryFactory(new ProportionalHistoryFactory("Flaws and Drawbacks", -1.0));
		}
	}
	
	private int getDrawbackOrFlawNumber(){
		int age = getProperty("Age").getValue().getInt();
		if(age<=9){
			return 0;
		}else if(age<=13){
			return 0;
		}else if(age<=18){
			return 0;
		}else if(age<=30){
			return 1;
		}else if(age<=50){
			return 1;
		}else if(age<=70){
			return 2;
		}else if(age<=80){
			return 3;
		}else{
			return 4;
		}
	}
	
	private int getFlawNumber(){
		int age = getProperty("Age").getValue().getInt();
		if(age<=9){
			return 0;
		}else if(age<=13){
			return 0;
		}else if(age<=18){
			return 0;
		}else if(age<=30){
			return 0;
		}else if(age<=50){
			return 1;
		}else if(age<=70){
			return 1;
		}else if(age<=80){
			return 2;
		}else{
			return 3;
		}
	}
	
	private Collection<String>  getCompulsoryFlaw(){
		int age = getProperty("Age").getValue().getInt();
		if(age<=9){
			return new ArrayList<String>();
		}else if(age<=13){
			return new ArrayList<String>();
		}else if(age<=18){
			return new ArrayList<String>();
		}else if(age<=30){
			return new ArrayList<String>();
		}else if(age<=50){
			return Arrays.asList("Agility", "Athletics", "Endurance");
		}else if(age<=70){
			return Arrays.asList("Agility", "Athletics", "Awareness", "Cunning", "Endurance", "Fighting", "Marksmanship");
		}else if(age<=80){
			return Arrays.asList("Agility", "Athletics", "Awareness", "Cunning", "Endurance", "Fighting", "Marksmanship");
		}else{
			return Arrays.asList("Agility", "Athletics", "Awareness", "Cunning", "Endurance", "Fighting", "Marksmanship");
		}
	}
	
}
