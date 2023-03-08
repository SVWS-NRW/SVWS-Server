<script setup lang="ts">
</script>

<template>
	<Story title="SVWS UI/Layout/Content Card">
		<svws-ui-content-card title="Basisdaten">
			<template #actions>
				<svws-ui-button>Test</svws-ui-button>
				<svws-ui-button>Test</svws-ui-button>
			</template>
			<div class="flex flex-row items-start space-x-4">
				<!--
				<div class="w-24">
					<svws-ui-avatar />
				</div>
				-->
				<div class="flex-grow grid gap-3 grid-cols-1 md:grid-cols-2">
					<svws-ui-text-input type="text" placeholder="Vorname" />
					<svws-ui-text-input type="text" placeholder="Alle Vornamen" />
					<svws-ui-text-input type="text" placeholder="Nachname" />
					<svws-ui-text-input type="text" placeholder="Zusatz" />
					<svws-ui-text-input type="text" placeholder="Adresse" />
					<svws-ui-text-input type="text" placeholder="Wohnort" />
				</div>
			</div>
		</svws-ui-content-card>
	</Story>
</template>
