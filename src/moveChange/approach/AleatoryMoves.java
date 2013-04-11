package moveChange.approach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import moveChange.basic.AllEntitiesMapping;
import moveChange.methods.AllMethods;
import moveChange.methods.Method;
import moveChange.utils.MoveMethod;
import moveChange.utils.PrintOutput;

import org.eclipse.jdt.core.IMethod;

public class AleatoryMoves {

	AllEntitiesMapping entities = AllEntitiesMapping.getInstance();
	AllMethods allMethods;
	List<Integer> sourceClassUsed;
	List<Integer> targetClassUsed;
	Map<Method, String> movimentos;
	int numberOfClass;
	int cont = 0;

	public AleatoryMoves(AllMethods allMethods, int numberOfClass) {
		// TODO Auto-generated constructor stub
		this.sourceClassUsed = new ArrayList<Integer>();
		this.targetClassUsed = new ArrayList<Integer>();
		this.allMethods = allMethods;
		this.movimentos = new HashMap<Method, String>();
		this.numberOfClass = numberOfClass;
		// changeProgram(numberOfClass);

	}

	public void changeProgram(String activeProjectName) {
		// TODO Auto-generated method stub

		System.out.println("numero de classe " + numberOfClass);

		final int numberofMoves = (int) (0.1 * (numberOfClass));
		
		PrintOutput.write("Num metodos devem mover " + (int) (0.03 * (numberOfClass))+"\n", "needDo" + activeProjectName);

		List<Method> methodList = allMethods.getAllMethodsList();

		Random random = new Random();

		while (movimentos.size() < numberofMoves && cont < 10000) {
			System.out.println("Numero de moves poss " + movimentos.size());
			int candidateIndex = random.nextInt(methodList.size());
			Method sourceMethod = methodList.get(candidateIndex);
			if (allMethods.getMoveIspossible().contains(
					sourceMethod.getNameID())) {
				tryMove(sourceMethod);
				cont++;
			}

		}

		System.out.println("MOVIMENTOS");
		Iterator<Entry<Method, String>> it = movimentos.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Method, String> entry = it.next();

			MoveMethod.executeMove(allMethods.getIMethod(entry.getKey()),
					entry.getValue());
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
			System.out.println();

			writeMoves(entry.getKey(), entry.getValue(), activeProjectName);

		}

	}

	private void writeMoves(Method sourceMethod, String classe,
			String activeProjectName) {
		// TODO Auto-generated method stub
		String meString = sourceMethod.toString();
		PrintOutput.write("metodo " + meString + " movido para " + classe
				+ "\n", "moveDone" + activeProjectName);

		String parts[] = meString.split("::", 2);
		PrintOutput.write("metodo " + classe + "::" + parts[1] + " deve mover "
				+ parts[0] + "\n", "needDo" + activeProjectName);

	}

	private void tryMove(Method sourceMethod) {
		// TODO Auto-generated method stub

		final int TAMMINIMO = 4;
		int dependeciesSize = sourceMethod.getMethodsDependencies().size();

		if (!sourceClassUsed.contains(sourceMethod.getSourceClassID())
				&& dependeciesSize > TAMMINIMO) {
			List<String> possibilities = MoveMethod
					.getpossibleRefactoring(allMethods.getIMethod(sourceMethod));
			if (possibilities.size() > 0
					&& canChooseDestiny(possibilities, sourceMethod)) {
				sourceClassUsed.add(sourceMethod.getSourceClassID());
				cont = 0;
				return;
			}
		}

		return;
	}

	private boolean canChooseDestiny(List<String> possibilities,
			Method sourceMethod) {
		// TODO Auto-generated method stub

		//treaty(possibilities);

		IMethod imethodSource = allMethods.getIMethod(sourceMethod);

		String sourcePackge = entities.getByID(sourceMethod.getNameID());
		sourcePackge = sourcePackge.substring(0,
				sourcePackge.lastIndexOf(".", sourcePackge.length()));

		Random rand = new Random();
		boolean samePackge;
		String candidate;
		Integer targetID;
		Set<Integer> tried = new HashSet<Integer>();

		do {

			if (tried.size() == possibilities.size()) {
				return false;
			}

			samePackge = false;

			int indexCandidate = rand.nextInt(possibilities.size());
			candidate = possibilities.get(indexCandidate);

			String candidatePackge = candidate.substring(0,
					candidate.lastIndexOf(".", candidate.length()));

			if (sourcePackge.equals(candidatePackge)) {
				samePackge = true;
			}

			tried.add(indexCandidate);
			targetID = entities.getByName(treaty(candidate));
			
		} while (samePackge
				|| !MoveMethod.isValidCandidate(imethodSource, candidate)
				|| targetClassUsed.contains(targetID));

		if (!movimentos.containsKey(sourceMethod)) {
			movimentos.put(sourceMethod, candidate);
			targetClassUsed.add(targetID);
			return true;
		}

		return false;
	}

	private static String treaty(String candidate) {
		// TODO Auto-generated method stub

		

			int indexBegin = candidate.indexOf('<');

			if (indexBegin > 0) {
				String treatyName = candidate.substring(0, indexBegin);
				return treatyName;

			}

			indexBegin = candidate.indexOf('[');
			if (indexBegin > 0) {
				String treatyName = candidate.substring(0, indexBegin);
				return treatyName;

			}
		

		return candidate;

	}
}