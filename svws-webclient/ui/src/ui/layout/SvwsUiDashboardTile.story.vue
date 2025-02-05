<template>
	<Story title="Dashboard Tile" id="svws-ui-dashboard-tile" icon="ri:dashboard-line" auto-props-disabled responsive-disabled>
		<Variant title="Default" id="Default" source=" ">
			<div class="p-4">
				<div class="svws-ui-dashboard">
					<svws-ui-dashboard-tile title="Default">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="2 col span" :span="2">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Color Dark" color="dark">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Full span" span="full">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="With Numbers" number="100" number-label="Label">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="With Numbers Dark" number="2000" number-label="Label" color="dark">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Clickable" clickable>Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Transparent" number="100" number-label="Label" color="transparent">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
				</div>
			</div>
			<div class="p-4 page--statistik">
				<div class="mt-10 mb-4">Statistik:</div>
				<div class="svws-ui-dashboard">
					<svws-ui-dashboard-tile title="Default">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="2 col span" :span="2">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Color Dark" color="dark">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Full span" span="full">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="With Numbers" number="100" number-label="Label">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="With Numbers Dark" number="2000" number-label="Label" color="dark">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Clickable" clickable>Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Transparent" number="100" number-label="Label" color="transparent">Lorem ipsum dolor, sit amet consectetur adipisicing elit.</svws-ui-dashboard-tile>
				</div>
			</div>
		</Variant>
	</Story>
</template>

<script setup lang="ts">

</script>

<style lang="postcss">

	@reference "../../assets/styles/index.css";

</style>
