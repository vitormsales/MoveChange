package moveChange.approach;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import moveChange.basic.AllEntitiesMapping;
import moveChange.methods.AllMethods;
import moveChange.methods.Method;
import moveChange.utils.MoveMethod;

public class AleatoryMoves {

	AllEntitiesMapping entities = AllEntitiesMapping.getInstance();
	AllMethods allMethods;
	List<Integer> methodsToMove;
	List<Integer> classUsed;

	public AleatoryMoves(AllMethods allMethods, int numberOfClass) {
		// TODO Auto-generated constructor stub
		this.classUsed = new ArrayList<Integer>();
		this.methodsToMove = new ArrayList<Integer>();
		this.allMethods = allMethods;
		changeProgram(numberOfClass);

	}

	private void changeProgram(int numberOfClass) {
		// TODO Auto-generated method stub

		System.out.println("numero de classe " + numberOfClass);

		final int numberofMoves = (int) (0.05 * (numberOfClass));

		List<Method> methodList = allMethods.getAllMethodsList();

		Random random = new Random(0);
		while (methodsToMove.size() < numberofMoves) {

			int candidateIndex = random.nextInt(methodList.size());
			Method sourceMethod = methodList.get(candidateIndex);
			if (canMove(sourceMethod)) {
				methodsToMove.add(sourceMethod.getNameID());
			}
		}

		for (Integer id : methodsToMove) {
			// MoveMethod.randomMove(allMethods.getIMethod(id));
			// System.out.println(entities.getByID(id));

		}

		// System.out.println("executa emm "
		// + entities.getByID(methodsToMove.get(0)));
		// MoveMethod.randomMove(allMethods.getIMethod(methodsToMove.get(0)));

	}

	private boolean canMove(Method sourceMethod) {
		// TODO Auto-generated method stub

		final int TAMMINIMO = 4;
		int dependeciesSize = sourceMethod.getMethodsDependencies().size();

		if (!classUsed.contains(sourceMethod.getSourceClassID())
				&& dependeciesSize > TAMMINIMO) {
			List<String> possibilities = MoveMethod
					.getpossibleRefactoring(allMethods.getIMethod(sourceMethod));
			if (possibilities.size() > 0) {

				chooseDestiny(possibilities, sourceMethod);

				classUsed.add(sourceMethod.getSourceClassID());
				return true;
			}
		}

		return false;
	}

	private void chooseDestiny(List<String> possibilities, Method sourceMethod) {
		// TODO Auto-generated method stub

		System.out.println(sourceMethod);
		String sourcePackge = entities.getByID(sourceMethod.getNameID());
		sourcePackge = sourcePackge.substring(0,
				sourcePackge.lastIndexOf(".", sourcePackge.length()));

		//System.out.println("sourcePackge " + sourcePackge);
		
		for (String pos : possibilities) {
			System.out.println(pos);
			System.out.println("Pacote " + pos.lastIndexOf(".", pos.length()));
			System.out.println(pos.substring(0,
					pos.lastIndexOf(".", pos.length())));

		}

		System.out.println();

	}
}
