package manager;

//C Наступившим Новым Годом!) спасибо большое за помощь в обучении, ты мне помогаешь больше, чем кто либо другой в ЯП,
//отдельно благодарен за детальный разбор моих ошибок. Всего хорошего, в новом году)

class Node <T> {

    public T data;
    public Node<T> next;
    public Node<T> prev;

    public Node(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    }
