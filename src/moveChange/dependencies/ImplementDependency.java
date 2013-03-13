package moveChange.dependencies;

import moveChange.enums.DependencyType;

public class ImplementDependency extends DeriveDependency {
	public ImplementDependency(String classNameA, String classNameB) {
		super(classNameA,classNameB);
	}
		
	@Override
	public DependencyType getDependencyType() {
		return DependencyType.IMPLEMENT;
	}
}