<?php
$autoloader = require "vendor/autoload.php";
$autoloader->loadClass("\\app\\Migration\\Migration");
$s = require __DIR__ . '/src/settings.php';

return [
  'paths' => [
    'migrations' => 'migrations'
  ],
  'migration_base_class' => '\app\Migration\Migration',
  'environments' => [
    'default_migration_table' => 'phinxlog',
    'default_database' => 'Akount',
    'Akount' => [
      'adapter' => 'mysql',
      'host' => $s["settings"]['db']['host'],
      'name' => $s["settings"]['db']['database'],
      'user' => $s["settings"]['db']['username'],
      'pass' => $s["settings"]['db']['password'],
      'port' => 3306
    ]
  ]
];