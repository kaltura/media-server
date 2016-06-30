<?php
require_once(__DIR__ . '/KalturaWowzaServer/build/tmp/github-php-client/GitHubClient.php');

function uploadAsset($client, $filename, $owner, $repo, $releaseId, $contentType) {
    echo "Uploading asset: " . $filename . "\n";
    $name = basename($filename);
    $client->repos->releases->assets->upload($owner, $repo, $releaseId, $name, $contentType, $filename);
    echo "Asset uploaded: $filename\n";
}

$owner = 'kaltura';
$repo = 'media-server';
$username = $argv[1];
$password = $argv[2];
$version = $argv[3];
$tag_name = "rel-$version";

// Get the tag of the latest *local* commit
$target_commitish = exec("git rev-parse HEAD 2>&1", $output, $retVal);
if ( $retVal !== 0 )
{
    echo implode("\n", $output);
    exit( $retVal );
}

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

$jars = glob(__DIR__ . "/KalturaWowzaServer/build/tmp/artifacts/Kaltura*.jar");
foreach ($jars as $jar) {
    uploadAsset($client, $jar, $owner, $repo, $releaseId,'application/java-archive');
}

//upload the zip distribution
$filePath = __DIR__ . "/KalturaWowzaServer/build/distributions/KalturaWowzaServer-install-$version.zip";
uploadAsset($client, $filePath, $owner, $repo, $releaseId,'application/zip');

exit(0);