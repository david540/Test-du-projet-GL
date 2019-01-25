package fr.ensimag.deca.tree;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import fr.ensimag.deca.CompilationType;
import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.SymbolTable;
import org.mockito.Mock;

public class TestUnitaireGencode {
	final Type INT = new IntType(null);
	final Type FLOAT = new FloatType(null);
	DecacCompiler compiler = new DecacCompiler(null,null);
	
	@Mock
    CompilerOptions cOptions = new CompilerOptions();
	
	@Before
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(cOptions, null);
        when(cOptions.getCompilationType()).thenReturn(CompilationType.IMA_68000);
    }


	private static DeclVar declInt(DecacCompiler compiler, Identifier var, int value) {
        var.setDefinition(new VariableDefinition(new IntType(compiler.getTable().create("int")), new Location(0, 0, null)));
        
        Identifier type = new Identifier(compiler.getTable().create("int"));
        type.setDefinition(new TypeDefinition(new IntType(compiler.getTable().create("int")), Location.BUILTIN));
        
        Initialization init = new Initialization(new IntLiteral(value));
        DeclVar decVar = new DeclVar(type, var, init);
        return decVar;
	}
	private static DeclVar declFloat(DecacCompiler compiler, Identifier var, float value) {
        var.setDefinition(new VariableDefinition(new FloatType(compiler.getTable().create("int")), new Location(0, 0, null)));
        
        Identifier type = new Identifier(compiler.getTable().create("float"));
        type.setDefinition(new TypeDefinition(new FloatType(compiler.getTable().create("float")), Location.BUILTIN));
        
        Initialization init = new Initialization(new FloatLiteral(value));
        DeclVar decVar = new DeclVar(type, var, init);
        return decVar;
	}
	public static AbstractProgram initTest1() {
        ListInst linst = new ListInst();
        AbstractProgram source =
            new Program(
                new ListDeclClass(),
                new Main(new ListDeclVar(),linst));
        ListExpr lexp1 = new ListExpr(), lexp2 = new ListExpr();
        linst.add(new Print(false,lexp1));
        linst.add(new Println(false,lexp2));
        lexp1.add(new StringLiteral("Hello I'm "));
        lexp1.add(new IntLiteral(21));
        lexp2.add(new StringLiteral(" and 1/2 = "));
        lexp2.add(new FloatLiteral(0.5f));
        return source;
    }

    public String gencodeSource(AbstractProgram source) {
        source.codeGenProgram(compiler);
        return compiler.displayIMAProgram();
    }
    @Test
    public void test1() {
        AbstractProgram source = initTest1();
        String result = gencodeSource(source);
        BashTester bashTester = new BashTester(result, "Hello I'm 21 and 1/2 = 5.00000e-01\n");
        try {
        	assertTrue(bashTester.executeTest());
        } catch(IOException e) {
        	System.out.println(e.getStackTrace());
        }
    }
    
    @Test
    public void launchTestVariable() {
    	
    	ListDeclVar declVariables = new ListDeclVar();
        ListInst instructions = new ListInst();
        SymbolTable symbolTable = new SymbolTable();

        AbstractProgram source = new Program(new ListDeclClass(), new Main(declVariables, instructions));

        ListExpr listPrint = new ListExpr();
        
        
        
        instructions.add(new Println(false, listPrint));
        Identifier a = new Identifier(compiler.getTable().create("a"));
        declVariables.add(declInt(compiler, a, 1));
        Identifier b = new Identifier(compiler.getTable().create("b"));
        declVariables.add(declInt(compiler, b, 1));
        Identifier c = new Identifier(compiler.getTable().create("c"));
        declVariables.add(declInt(compiler, c, -5));

        
        listPrint.add(new Minus(a, new IntLiteral(-1)));
        listPrint.add(new StringLiteral(" "));
        listPrint.add(new Minus(new IntLiteral(-1), b));
        listPrint.add(new StringLiteral(" "));
        listPrint.add(new Minus(c, new Minus(new IntLiteral(2), new IntLiteral(1))));
        listPrint.add(new StringLiteral(" "));
        listPrint.add(new Minus(c, a));
        listPrint.add(new StringLiteral(" "));
           
        String result = gencodeSource(source);
        BashTester bashTester = new BashTester(result, 
        		"2 -2 -6 -6 \n");
        try {
        	assertTrue(bashTester.executeTest());
        } catch(IOException e) {
        	System.out.println(e.getStackTrace());
        }
    }
    
    @Test
    public void launchTestVariableMultiply() {
    	
    	ListDeclVar declVariables = new ListDeclVar();
        ListInst instructions = new ListInst();
        SymbolTable symbolTable = new SymbolTable();

        AbstractProgram source = new Program(new ListDeclClass(), new Main(declVariables, instructions));

        ListExpr listPrint = new ListExpr();
        
        
        
        instructions.add(new Println(false, listPrint));
        Identifier x = new Identifier(compiler.getTable().create("x"));
        declVariables.add(declInt(compiler, x, 2));
        Identifier y = new Identifier(compiler.getTable().create("y"));
        declVariables.add(declFloat(compiler, y, 5.6f));

        
        listPrint.add(new Plus(new Multiply(new ConvFloat(x), y), new ConvFloat(new IntLiteral(3))));
        
        String result = gencodeSource(source);
        BashTester bashTester = new BashTester(result, 
        		"1.42000e+01\n");
        try {
        	assertTrue(bashTester.executeTest());
        } catch(IOException e) {
        	System.out.println(e.getStackTrace());
        }
    }
    
    @Test
    public void launchTestMinusFloatInt() {
    	ListDeclVar declVariables = new ListDeclVar();
        ListInst instructions = new ListInst();
        SymbolTable symbolTable = new SymbolTable();

        AbstractProgram source = new Program(new ListDeclClass(), new Main(declVariables, instructions));

        ListExpr listPrint = new ListExpr();

        listPrint.add(new Minus(new FloatLiteral(4.5f), new ConvFloat(new IntLiteral(-1))));
        instructions.add(new Println(false, listPrint));
        
        String result = gencodeSource(source);
        BashTester bashTester = new BashTester(result, 
        		"5.50000e+00\n");
        try {
        	assertTrue(bashTester.executeTest());
        } catch(IOException e) {
        	System.out.println(e.getStackTrace());
        }
    }
    
    @Test
    public void launchTestMinusFloatFloat() {
    	
    	ListDeclVar declVariables = new ListDeclVar();
        ListInst instructions = new ListInst();
        SymbolTable symbolTable = new SymbolTable();

        AbstractProgram source = new Program(new ListDeclClass(), new Main(declVariables, instructions));

        ListExpr listPrint = new ListExpr();

        listPrint.add(new Minus(new FloatLiteral(4.5f), new FloatLiteral(-1f)));
        instructions.add(new Println(false, listPrint));
        
        String result = gencodeSource(source);
        BashTester bashTester = new BashTester(result, 
        		"5.50000e+00\n");
        try {
        	assertTrue(bashTester.executeTest());
        } catch(IOException e) {
        	System.out.println(e.getStackTrace());
        }
    }
    

}
