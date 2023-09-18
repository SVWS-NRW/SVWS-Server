<template>
	<div class="page--content">
		<router-view :key="$route.hash" name="lernabschnittauswahl" />
		<router-view :key="$route.hash" />
	</div>
</template>

<style lang="postcss" scoped>
.page--content {
	grid-template-columns: 8.5rem minmax(50rem, 1fr);

	@media (min-width: 1440px) {
		grid-template-columns: 8.5rem minmax(62rem, 1fr) minmax(0, 1fr);
	}
}
</style>
<script setup>
</script>
