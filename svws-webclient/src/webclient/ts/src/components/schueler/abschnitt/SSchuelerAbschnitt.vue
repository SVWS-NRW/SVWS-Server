<template>
	<div class="flex flex-row">
		<div class="w-32 mr-2"> <router-view :key="$route.hash" name="lernabschnittauswahl" /> </div>
		<div class="grow"> <router-view :key="$route.hash" /> </div>
	</div>
</template>

<script setup lang="ts">

</script>
