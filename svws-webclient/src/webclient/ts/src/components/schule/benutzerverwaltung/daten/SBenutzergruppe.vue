<template>
	<div class="flex flex-row flex-grow">
		<div class="mr-4">
			<s-card-benutzergruppe-daten />
		</div>
		<div>
			<s-card-benutzergruppe-kompetenzen />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { App } from "~/apps/BaseApp";

</script>
