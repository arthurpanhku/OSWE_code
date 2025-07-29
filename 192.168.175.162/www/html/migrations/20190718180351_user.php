<?php

use \app\Migration\Migration;

class User extends Migration
{
    public function up()  {
        $this->schema->create('users', function(Illuminate\Database\Schema\Blueprint $table){
            // Auto-increment id 
            $table->increments('id');
            $table->string('email');
            $table->string('name');
            $table->binary('password');
            $table->text('bio');
            $table->longText('avatar')->nullable();
            $table->boolean('isAdmin')->default(false);
            // Required for Eloquent's created_at and updated_at columns 
            $table->timestamps();
            $table->unique('email');
        });
    }
    public function down()  {
        $this->schema->drop('users');
    }
}
