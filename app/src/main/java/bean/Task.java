package bean;


    public class Task{
        private String name;
        private String task;
        public Task(String name,String task){
            this.name = name;
            this.task = task;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }
    }

