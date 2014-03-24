<?php
require_once(__DIR__ . '/github-php-client/GitHubClient.php');

$owner = 'kaltura';
$repo = 'media-server';
$username = $argv[1];
$password = $argv[2];
$version = $argv[3];

$tag_name = "rel-$version";
$target_commitish = null;
$name = "v$version";
$body = "Release version $version";
$draft = false;
$prerelease = true;

$client = new GitHubClient();
$client->setCredentials($username, $password);

$release = $client->repos->releases->create($owner, $repo, $tag_name, $target_commitish, $name, $body, $draft, $prerelease);
/* @var $release GitHubReposRelease */
$releaseId = $release->getId();
echo "Release created with id $releaseId\n";

$name = "KalturaWowzaServer-$version.jar";
$filePath = __DIR__ . "/bin/$name";
$contentType = 'application/java-archive';

$client->repos->releases->assets->upload($owner, $repo, $releaseId, $name, $contentType, $filePath);
echo "JAR file uploaded: $filePath\n";

exit(0);