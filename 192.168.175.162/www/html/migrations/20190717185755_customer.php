<?php

use \app\Migration\Migration;

class Customer extends Migration
{
    public function up()  {
        $this->schema->create('customers', function(Illuminate\Database\Schema\Blueprint $table){
            // Auto-increment id 
            $table->increments('id');
            $table->string('name');
            $table->string('address');
            $table->string('city');
            $table->string('state');
            $table->string('zip');
            $table->string('email');
            $table->integer('userid');
            // Required for Eloquent's created_at and updated_at columns 
            $table->timestamps();
        });
    }
    public function down()  {
        $this->schema->drop('customer');
    }
}
