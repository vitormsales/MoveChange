package envy;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
import moveChange.basic.AllEntitiesMapping;
=======
import movechange.basic.AllEntitiesMapping;
>>>>>>> fe9d8ee3b7cb91f56fa21b6af37eae38e732da28


public class InternalClass {

	private static InternalClass instance;
	private List<String> classNames;

	private InternalClass() {
		// TODO Auto-generated constructor stub
		this.classNames = new ArrayList<String>();
	}

	public static InternalClass getInstance() {
		if (instance == null) {
			instance = new InternalClass();
		}
		return instance;
	}

	// public int getInternalClassID(){
	// return 0;
	// }

	public void putNewInternalClass(String className) {
		classNames.add(className);
	}

	public List<String> getClassList() {
		return classNames;
	}

	public boolean isInternal(Integer id) {
		String candidate = AllEntitiesMapping.getInstance().getByID(id);
		//System.out.println("candidato "+ candidate);
		if (classNames.contains(candidate)) {
			//System.out.println("contem "+ candidate);
			return true;
		}
		return false;
	}
}
