<?php

use \app\Migration\Migration;

class Item extends Migration
{
    public function up()  {
        $this->schema->create('items', function(Illuminate\Database\Schema\Blueprint $table){
            // Auto-increment id 
            $table->increments('id');
            $table->decimal('price', 13, 2);
            $table->integer('quantity');
            $table->string('description');
            $table->integer('invoiceId');
            // Required for Eloquent's created_at and updated_at columns 
            $table->timestamps();
        });
    }
    public function down()  {
        $this->schema->drop('items');
    }
}
