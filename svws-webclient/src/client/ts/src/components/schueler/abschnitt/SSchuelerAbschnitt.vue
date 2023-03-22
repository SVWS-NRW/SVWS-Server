<template>
	<div class="leistungsdaten flex items-start gap-8">
		<div class="w-32">
			<!--TODO: Vertical Router Tab Bar Style für Abschnittsauswahl-->
			<router-view :key="$route.hash" name="lernabschnittauswahl" />
		</div>
		<router-view :key="$route.hash" />
	</div>
</template>

<script setup lang="ts">

</script>
