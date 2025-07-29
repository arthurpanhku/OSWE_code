<?php

use \app\Migration\Migration;

class RateLimiting extends Migration
{
    public function up()  {
        $this->schema->create('rate_limits', function(Illuminate\Database\Schema\Blueprint $table){
            // Auto-increment id 
            $table->string('userId', 255)->unique();
            $table->integer('amount');
            $table->timestamps();
        });
    }
    public function down()  {
        $this->schema->drop('rate_limits');
    }
}
