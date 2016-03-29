package controller;


import org.junit.Before;
import org.junit.Test;

public class DeleteOperationTest extends OperationTest {
    DeleteOperation op;
    AddOperation invOp;

    @Before
    public void setUp() {
        super.setUp();
        op = new DeleteOperation(this.todos, this.item);
        invOp = new AddOperation(this.todos, this.item);
    }

    @Test
    public void testAddInverse() {
        invOp.execute();
        hasAsFirstElement();
        op.execute();
        isEmptyTodos();
    }

    @Test
    @Override
    public void testExecute() {
        this.todos.add(this.item);
        hasAsFirstElement();
        op.execute();
        isEmptyTodos();
    }

    @Test
    @Override
    public void testInverse() {
        op.inverse();
        hasAsFirstElement();
        op.execute();
        isEmptyTodos();
    }

}
