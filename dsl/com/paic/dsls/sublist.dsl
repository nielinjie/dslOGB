dsl.requirement{
	name('sublist_fas')
	description(
		'sublist for fas system'
	)
	register{
		expCondition(exp:'owner.propertyA == "a" && context.contextA=="a"')
		sublistFeature{
			column(name:'column1')
			column(name:'column2')
			column(name:'c3')
		}
	}
}
