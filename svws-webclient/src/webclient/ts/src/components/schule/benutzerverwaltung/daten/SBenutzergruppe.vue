<template>
	<div class="flex flex-row gap-3">
		<div class="felx flex-col w-2/5">
			<s-card-benutzergruppe-daten />
			<s-benutzergruppe-add-benutzer />
		</div>

		<div class="w-3/5">
			<s-card-benutzergruppe-kompetenzen />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { App } from "~/apps/BaseApp";

</script>
