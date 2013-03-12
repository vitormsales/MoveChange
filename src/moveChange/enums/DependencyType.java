package moveChange.enums;

import moveChange.dependencies.AccessDependency;
import moveChange.dependencies.AnnotateDependency;
import moveChange.dependencies.CreateDependency;
import moveChange.dependencies.DeclareDependency;
import moveChange.dependencies.Dependency;
import moveChange.dependencies.DeriveDependency;
import moveChange.dependencies.ExtendDependency;
import moveChange.dependencies.HandleDependency;
import moveChange.dependencies.ImplementDependency;
import moveChange.dependencies.ThrowDependency;

public enum DependencyType {
	ACCESS("access", AccessDependency.class), USEANNOTATION("useannotation",
			AnnotateDependency.class), CREATE("create", CreateDependency.class), DECLARE(
			"declare", DeclareDependency.class), DERIVE("derive",
			DeriveDependency.class), EXTEND("extend", ExtendDependency.class), HANDLE(
			"handle", HandleDependency.class), IMPLEMENT("implement",
			ImplementDependency.class), THROW("throw", ThrowDependency.class), DEPEND(
			"depend", Dependency.class);

	private final String value;
	private final Class<? extends Dependency> dependencyClass;

	private DependencyType(String value,
			Class<? extends Dependency> dependencyClass) {
		this.value = value;
		this.dependencyClass = dependencyClass;
	}

	public String getValue() {
		return this.value;
	}

	public Class<? extends Dependency> getDependencyClass() {
		return this.dependencyClass;
	}

	public final Dependency createGenericDependency(String classNameA,
			String classNameB) {
		if (this == ACCESS) {
			return new AccessDependency(classNameA, classNameB);
		} else if (this == USEANNOTATION) {
			return new AnnotateDependency(classNameA, classNameB);
		} else if (this == CREATE) {
			return new CreateDependency(classNameA, classNameB);
		} else if (this == DECLARE) {
			return new DeclareDependency(classNameA, classNameB);
		} else if (this == EXTEND) {
			return new ExtendDependency(classNameA, classNameB);
		} else if (this == IMPLEMENT) {
			return new ImplementDependency(classNameA, classNameB);
		} else if (this == THROW) {
			return new ThrowDependency(classNameA, classNameB, null, null);
		}
		return null;
	}
}