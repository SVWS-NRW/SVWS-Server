<template>
	<div class="page--content page--content--single">
		<div class="leistungsdaten flex items-start gap-16">
			<div class="w-32 flex-shrink-0">
				<!--TODO: Vertical Router Tab Bar Style für Abschnittsauswahl-->
				<router-view :key="$route.hash" name="lernabschnittauswahl" />
			</div>
			<router-view :key="$route.hash" />
		</div>
	</div>
</template>

<style lang="postcss">
.leistungsdaten .data-table {
	@apply overflow-visible;
}
</style>
