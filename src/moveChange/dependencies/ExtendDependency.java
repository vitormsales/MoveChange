package moveChange.dependencies;

import moveChange.enums.DependencyType;


public class ExtendDependency extends DeriveDependency {
	public ExtendDependency(String classNameA, String classNameB) {
		super(classNameA,classNameB);
	}
		
	@Override
	public DependencyType getDependencyType() {
		return DependencyType.EXTEND;
	}
}