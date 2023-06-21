<template>
	<div class="page--content page--content--abschnitt">
		<router-view :key="$route.hash" name="lernabschnittauswahl" />
		<router-view :key="$route.hash" />
	</div>
</template>

<style lang="postcss" scoped>
.page--content--abschnitt {
	grid-template-columns: 8.5rem 1fr;

	@media (min-width: 1440px) {
		grid-template-columns: 8.5rem 1fr;
	}
}
</style>

<script setup>
</script>
