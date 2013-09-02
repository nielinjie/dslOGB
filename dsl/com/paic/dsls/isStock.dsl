dsl.requirement{
    name('isStock')
    description('是否关联交易')
    condition(exp:'(owner.sortId==1 || owner.sortId==11 ) && owner.taskComp.contains("PA002")')

    dynamicProperty{
        name('isStockTrade')
        cName('关联交易')
        type(java.lang.String.class)
        stringValidator()
        stringEditor()
        maxLength(10)
        minLength(1)
        isRequired(true)
        defaultValue(true)
        renderMap(key:'writable',value:$selectorRenderer())
        renderMap(key:'readOnly')
        activeEvent{
            change('var ....')
        }
    }

    show{
        flag(function:'submitTask',flag:'writable')
        flag(function:'approval',flag:'readOnly')
    }

    mail{
        handleMap(key:'New_Submit',value:'mailHandle')
        handleMap(key:'Edit_Submit',value:'mailHandle')
    }



}