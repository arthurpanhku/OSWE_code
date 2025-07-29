<?php

use \app\Migration\Migration;

class Invoice extends Migration
{
    public function up()  {
        $this->schema->create('invoices', function(Illuminate\Database\Schema\Blueprint $table){
            // Auto-increment id 
            $table->increments('id');
            $table->decimal('total', 13, 2);
            $table->date('dueDate');
            $table->date('createdDate');
            $table->float('tax');
            $table->integer('customerId');
            $table->string('description');
            $table->integer('userid');
            $table->boolean('paid')->default(false);
            $table->string('proof')->nullable();
            // Required for Eloquent's created_at and updated_at columns 
            $table->timestamps();
        });
        $this->execute("ALTER TABLE invoices AUTO_INCREMENT = 1000000;");
    }
    public function down()  {
        $this->schema->drop('invoices');
    }
}
