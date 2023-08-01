<script setup lang="ts">
</script>

<template>
	<Story title="Content Card" id="svws-ui-content-card" group="app" :layout="{type: 'grid', width: '90%'}" icon="ri:profile-line">
		<svws-ui-content-card title="Title">
			<template #actions>
				<svws-ui-button type="secondary">Button</svws-ui-button>
				<svws-ui-button>Button</svws-ui-button>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input type="text" placeholder="Vorname" span="full" />
				<svws-ui-text-input type="text" placeholder="Alle Vornamen" />
				<svws-ui-text-input type="text" placeholder="Nachname" />
				<svws-ui-spacing />
				<svws-ui-text-input type="text" placeholder="Zusatz" />
				<svws-ui-text-input type="text" placeholder="Adresse" />
				<svws-ui-text-input type="text" placeholder="Wohnort" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</Story>
</template>
