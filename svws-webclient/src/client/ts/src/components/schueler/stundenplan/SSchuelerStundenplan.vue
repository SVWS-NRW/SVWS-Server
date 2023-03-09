<template>
	<div class="flex flex-row">
		<div class="w-[26rem] mr-2"> <router-view :key="$route.hash" name="stundenplanauswahl" /> </div>
		<div> <router-view :key="$route.hash" /> </div>
	</div>
</template>

<script setup lang="ts">

</script>